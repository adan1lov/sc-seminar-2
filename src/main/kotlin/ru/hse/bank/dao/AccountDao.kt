package ru.hse.bank.dao

import ru.hse.bank.entity.AccountEntity

interface AccountDao {

    fun findAccountByName(name: String): AccountEntity

    fun increaseAccountSumByName(name: String, value: Long): AccountEntity

    fun saveAccount(accountEntity: AccountEntity): Boolean
}
