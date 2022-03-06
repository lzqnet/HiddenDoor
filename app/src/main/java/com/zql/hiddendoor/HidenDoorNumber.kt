package com.zql.hiddendoor

class HidenDoorNumber {
    companion object{
        var numberList:List<Int> = listOf(1,2,1,2,3,4,4)
    }

}

enum class Position(val value: Int) {
    LEFT_TOP(1),LEFT_BOTTOM(2),RIGHT_BOTTOM(3),RIGHT_TOP(4)

}