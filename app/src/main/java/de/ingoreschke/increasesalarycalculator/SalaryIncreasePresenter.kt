package de.ingoreschke.increasesalarycalculator

class SalaryIncreasePresenter(private val salaryIncreaseInteractor: SalaryIncreaseInteractor) {

    fun calculateSalaryIncrease(salary: Double, increasePercentage: Double): Double {
        return salaryIncreaseInteractor.calcIncreasedSalary(salary, increasePercentage)
    }
}