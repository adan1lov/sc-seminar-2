package ru.hse.bank.service

import ru.hse.bank.entity.AccountEntity

interface AccountService {

    fun saveAccount(accountEntity: AccountEntity): Boolean

    fun findAccountByName(name: String): AccountEntity

    fun topUpBalance(accountEntity: AccountEntity, value: Long): AccountEntity

    fun transfer(from: AccountEntity, to: AccountEntity, value: Long)
}
