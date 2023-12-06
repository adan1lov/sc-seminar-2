package ru.hse.bank.dao

import ru.hse.bank.entity.AccountEntity

interface AccountDao {

    fun findAccountByName(name: String): AccountEntity

    fun increaseAccountSumByName(name: String, sum: Long): AccountEntity

    fun saveAccount(accountEntity: AccountEntity): Boolean

    fun transfer(nameFrom: String, nameTo: String, sum: Long) : Pair<AccountEntity, AccountEntity>
    fun transfer(accountEntityFrom: AccountEntity, accountEntityTo: AccountEntity, sum: Long) : Pair<AccountEntity, AccountEntity>
}
