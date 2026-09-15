package com.practice.efubaccount.account.repository;

import com.practice.efubaccount.account.domain.AccountDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountDocumentRepository extends MongoRepository<AccountDocument, String> {

}