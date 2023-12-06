package ru.hse.bank

import ru.hse.bank.dao.FileSystemAccountDao
import ru.hse.bank.entity.AccountEntity
import java.awt.Frame

fun main() {

    var accountDao = FileSystemAccountDao()
    val alice = AccountEntity(name = "Alice", sum = 300)
    val bob = AccountEntity(name = "Bob", sum = 1000)
    val FrankAbagnale = AccountEntity(name = "FrankAbagnale", sum = 999999)

    accountDao.saveAccount(alice)
    accountDao.saveAccount(bob)
    accountDao.saveAccount(FrankAbagnale)

    accountDao.increaseAccountSumByName("Alice", 200)

    accountDao.transfer("FrankAbagnale", "Bob", 3000)
    accountDao.transfer("FrankAbagnale", "Alice", 500)
//
//    accountDao.saveAccount(alice)
//    accountDao.increaseAccountSumByName("Alice", 100)
//
//    var acc = accountDao.findAccountByName("Alice")
//    println(acc.sum)
}
