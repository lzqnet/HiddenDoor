package com.zql.hiddendoor

class HidenDoorNumber {
    companion object{
        var numberList1:List<Int> = listOf(1,2,1,2,3,4,4)
        var numberList2:List<Int> = listOf(1,2,1,2)
        var numberList3:List<Int> = listOf(1,2,3,4,4)
        var numberList4:List<Int> = listOf(4,4)

    }

}

enum class Position(val value: Int) {
    LEFT_TOP(1),LEFT_BOTTOM(2),RIGHT_BOTTOM(3),RIGHT_TOP(4)

}