package ru.hse.bank.service

import ru.hse.bank.dao.AccountDao
import ru.hse.bank.dao.FileSystemAccountDao
import ru.hse.bank.dao.exception.AccountNotFoundException
import ru.hse.bank.entity.AccountEntity
import ru.hse.bank.service.exception.NotEnoughMoneyException

class AccountServiceImpl(private val accountDao: AccountDao): AccountService {

    override fun saveAccount(accountEntity: AccountEntity): Boolean
        = accountDao.saveAccount(accountEntity)


    override fun findAccountByName(name: String): AccountEntity
        = accountDao.findAccountByName(name)


    override fun topUpBalance(accountEntity: AccountEntity, value: Long): AccountEntity
        = accountDao.increaseAccountSumByName(accountEntity.name, value)


    override fun transfer(from: AccountEntity, to: AccountEntity, value: Long) {
        if (from.sum < value){
            throw NotEnoughMoneyException("Account ${from.name} has not enough money")
        }

        try {
            findAccountByName(from.name)
            findAccountByName(to.name)
        } catch (ex: AccountNotFoundException){
            accountDao.saveAccount(from)
            accountDao.saveAccount(to)
        }

        accountDao.increaseAccountSumByName(from.name, -value)
        accountDao.increaseAccountSumByName(to.name, value)
    }
}
