package ru.hse.bank

import ru.hse.bank.dao.AccountDao
import ru.hse.bank.dao.RuntimeAccountDao
import ru.hse.bank.entity.AccountEntity

class Bank(accountDao: AccountDao = RuntimeAccountDao()) {
    private var _accountDao = accountDao

    // userFrom gives money to userTo
    public fun <T, U> transfer(userFrom: T, userTo: U, value: Long): Boolean {
        if (value < 0) {
            return transfer(userTo, userFrom, -value)
        }
        val accountFrom: Any // find accounts
        val accountTo: Any
        try {
            accountFrom = if (userFrom is AccountEntity) userFrom else _accountDao.findAccountByName(userFrom as String)
            accountTo = if (userTo is AccountEntity) userTo else _accountDao.findAccountByName(userTo as String)
        } catch (ex: Exception) { // if there is not at least one account
            return false
        }
        if (accountFrom.sum < value) { // if userFrom do not have enough money
            return false
        }
        accountFrom.sum -= value // transfer
        accountTo.sum += value
        _accountDao.saveAccount(accountFrom) // saving results
        _accountDao.saveAccount(accountTo)
        return true
    }

    // gets account by the name
    public fun getAccountByName(name: String): AccountEntity? {
        return try { // if account with this name in _accountDao
            _accountDao.findAccountByName(name)
        } catch (ex: Exception) { // if there is no account with this name
            return null
        }
    }

    // saving account in _accountDao
    public fun <T> saveAccount(userIn: T): Boolean {
        return try {
            val user =
                if (userIn is AccountEntity) userIn else _accountDao.findAccountByName(userIn as String) // there can be cast exception
            _accountDao.saveAccount(user)
        } catch (ex: Exception) {
            return false
        }
    }

}