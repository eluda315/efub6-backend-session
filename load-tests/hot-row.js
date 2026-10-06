import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend } from 'k6/metrics';

const rowUpdateDuration = new Trend('row_update_duration', true);

const baseUrl = (__ENV.BASE_URL || 'http://localhost:8080').replace(/\/$/, '');
const mode = (__ENV.HOT_ROW_MODE || 'single').toLowerCase();
const poolSize = Math.min(readNumber('POST_POOL_SIZE', 100), 100);
const maxP95Ms = readNumber('MAX_P95_MS', 500);
const maxErrorRate = readNumber('MAX_ERROR_RATE', 0.01);

if (!['single', 'sharded'].includes(mode)) {
  throw new Error(`HOT_ROW_MODE must be single or sharded: ${mode}`);
}

export const options = {
  stages: [
    { duration: __ENV.RAMP_UP_DURATION || '10s', target: readNumber('TARGET_VUS', 20) },
    { duration: __ENV.HOLD_DURATION || '20s', target: readNumber('TARGET_VUS', 20) },
    { duration: __ENV.RAMP_DOWN_DURATION || '10s', target: 0 },
  ],
  thresholds: {
    // checks, 성공 요청 p95, HTTP 오류율 기준
    // http.js에서 작성한 기준을 카운터 쓰기 실습에도 적용한다.
    

  },
  userAgent: 'efub6-k6-hot-row/1.0',
};

export function setup() {
  const response = http.get(`${baseUrl}/posts?page=0&size=${poolSize}`);
  const ok = check(response, {
    'post pool status is 200': (res) => res.status === 200,
  });

  if (!ok) {
    throw new Error(`Cannot load the post pool: HTTP ${response.status}`);
  }

  const postIds = response.json('posts.#.postId');
  if (!postIds || postIds.length === 0) {
    throw new Error('No posts found. Run seedLoadTestData first.');
  }

  return { postIds };
}

export default function (data) {
  // // 같은 게시글에 single/sharded 요청
  // // 두 모드 모두 같은 인기 게시글을 요청한다. 달라지는 것은 카운터 저장 방식뿐이다.
  // const postId = data.postIds[0];
  // const counterPath = mode === 'single' ? 'single-counter' : 'sharded-counter';
  // const response = http.get(`${baseUrl}/load-test/posts/${postId}/views/${counterPath}`, {
  //   tags: { counter_strategy: mode },
  //   responseCallback: http.expectedStatuses(204),
  //   timeout: __ENV.REQUEST_TIMEOUT || '5s',
  // });

  // // 성공 요청 시간 수집, 204 검증, 대기
  // if (response.status === 204) {
  //   rowUpdateDuration.add(response.timings.duration, { counter_strategy: mode });
  // }
  // check(response, {
  //   'status is 204': (res) => res.status === 204,
  // });

  // sleep(readNumber('SLEEP_SECONDS', 0.1));
}

function readNumber(name, fallback) {
  const rawValue = __ENV[name];
  if (rawValue === undefined || rawValue === '') {
    return fallback;
  }

  const value = Number(rawValue);
  if (!Number.isFinite(value) || value < 0) {
    throw new Error(`${name} must be a non-negative number: ${rawValue}`);
  }
  return value;
}
