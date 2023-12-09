package ru.hse.bank

import ru.hse.bank.dao.FileSystemAccountDao
import ru.hse.bank.entity.AccountEntity
import ru.hse.bank.service.AccountService
import ru.hse.bank.service.AccountServiceImpl

fun main() {

    var accountService = AccountServiceImpl(FileSystemAccountDao())

    val alice = AccountEntity(name = "Alice", sum = 0)
    val bob = AccountEntity(name = "Bob", sum = 200)

    accountService.saveAccount(alice)

    accountService.transfer(alice, bob, 100)

}
