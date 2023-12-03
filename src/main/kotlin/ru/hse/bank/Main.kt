package ru.hse.bank


import ru.hse.bank.dao.FileSystemAccountDao
import ru.hse.bank.entity.AccountEntity

fun main() {
    var bank = Bank(FileSystemAccountDao())
    var alice = bank.getAccountByName("Alice")
    if (alice == null) {
        alice = AccountEntity("Alice", 1000)
        bank.saveAccount(alice)
    }
    var oleg = bank.getAccountByName("Oleg")
    if (oleg == null) {
        oleg = AccountEntity("Oleg", 1200)
        bank.saveAccount(oleg)
    }
    if (bank.transfer(oleg, alice, 300)) {
        println("Successful transfer, check balance into files")
    } else {
        println("Something went wrong, may be user do not have enough money")
    }
}
