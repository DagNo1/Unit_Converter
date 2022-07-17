package converter

enum class TYPE {
    WEIGHT,
    LENGTH,
    TEMPERATURE,
    NULL
}
enum class UNIT(val unitNames: String, val conversion: Double, val type: TYPE) {
    METER("meters", 1.0, TYPE.LENGTH),
    KILOMETER("kilometers", 1000.0, TYPE.LENGTH),
    CENTIMETER("centimeters", 0.01, TYPE.LENGTH),
    MILLIMETER("millimeters", 0.001, TYPE.LENGTH),
    MILE("miles", 1609.35, TYPE.LENGTH),
    YARD("yards", 0.9144, TYPE.LENGTH),
    FOOT("feet", 0.3048, TYPE.LENGTH),
    INCH("inches", 0.0254, TYPE.LENGTH),
    GRAM("grams", 1.0, TYPE.WEIGHT),
    KILOGRAM("kilograms", 1000.0, TYPE.WEIGHT),
    MILLIGRAM("milligrams", 0.001, TYPE.WEIGHT),
    POUND("pounds", 453.592, TYPE.WEIGHT),
    OUNCE("ounces", 28.3495, TYPE.WEIGHT),
    CELSIUS("degrees Celsius", 1.0, TYPE.TEMPERATURE),
    FAHRENHEIT("degrees Fahrenheit", 1.0, TYPE.TEMPERATURE),
    KELVINS("kelvins", 1.0, TYPE.TEMPERATURE),
    NULL("???", 0.0, TYPE.NULL);

}

fun main() {
    while (true) {
        print("Enter what you want to convert (or exit): ")
        val input = readln().lowercase().split(' ')
        if (input.contains("exit")) return
        val inputDissected = mutableListOf<String>()
        for (i in input.indices) if (input[i] != "degree" && input[i] != "degrees") inputDissected.add(input[i])
        if (inputDissected[0].toDoubleOrNull() == null) {
            println("Parse error")
            continue
        }
        val value = inputDissected[0].toDouble()
        val inputUnit = typeConversion(inputDissected[1])
        val outputUnit = typeConversion(inputDissected[3])
        if (inputUnit.type != outputUnit.type || inputUnit == UNIT.NULL || outputUnit == UNIT.NULL) {
            println("Conversion from ${inputUnit.unitNames} to ${outputUnit.unitNames} is impossible")
            continue
        }
        if (inputUnit.type == TYPE.LENGTH && value < 0) {
            println("Length shouldn't be negative")
            continue
        } else if (inputUnit.type == TYPE.WEIGHT && value < 0) {
            println("Weight shouldn't be negative")
            continue
        }
        val result = valueConversion(inputUnit, outputUnit, value)
        printResult(inputUnit, outputUnit, value, result)
    }
}
fun printResult(inputUnit: UNIT, outputUnit: UNIT,value: Double, result: Double) {
    var finalInput = inputUnit.unitNames
    var finalOutput = outputUnit.unitNames
    if(value == 1.0){
        finalInput = when(finalInput) {
            "feet" -> "foot"
            "inches" -> "inch"
            "degrees Celsius" -> "degree Celsius"
            "degrees Fahrenheit" -> "degree Fahrenheit"
            else -> finalInput.substringBefore('s')
        }
    }
    if(result == 1.0){
        finalOutput = when(finalOutput) {
            "feet" -> "foot"
            "inches" -> "inch"
            "degrees Celsius" -> "degree Celsius"
            "degrees Fahrenheit" -> "degree Fahrenheit"
            else -> finalOutput.substringBefore('s')
        }
    }
    println("$value $finalInput is $result $finalOutput\n")
}
fun valueConversion(inputUnit: UNIT, outputUnit: UNIT,_value: Double): Double {
    var value = _value
    if (inputUnit.type == TYPE.TEMPERATURE) {
        when(inputUnit) {
            UNIT.FAHRENHEIT -> value = (value - 32) * 5 / 9
            UNIT.KELVINS -> value -= 273.15
        }
    }
    var result= 1.0
    if (outputUnit.type != TYPE.TEMPERATURE) {
        result = (value * inputUnit.conversion) / outputUnit.conversion
    } else {
        when(outputUnit) {
            UNIT.FAHRENHEIT -> result = (value * 9 / 5) + 32
            UNIT.KELVINS -> result = value + 273.15
            UNIT.CELSIUS -> result = value * 1.0
        }
    }
    return result
}
fun typeConversion(unit: String) = when (unit.lowercase()) {
    "m", "meter", "meters" -> UNIT.METER
    "km", "kilometer", "kilometers" -> UNIT.KILOMETER
    "cm", "centimeter", "centimeters" -> UNIT.CENTIMETER
    "mm", "millimeter", "millimeters" -> UNIT.MILLIMETER
    "mi", "mile", "miles" -> UNIT.MILE
    "yd", "yard", "yards" -> UNIT.YARD
    "ft", "foot", "feet" -> UNIT.FOOT
    "in", "inch", "inches" -> UNIT.INCH
    "g", "gram", "grams" -> UNIT.GRAM
    "kg", "kilogram", "kilograms" -> UNIT.KILOGRAM
    "mg", "milligram", "milligrams" -> UNIT.MILLIGRAM
    "lb", "pound", "pounds" -> UNIT.POUND
    "oz", "ounce", "ounces" -> UNIT.OUNCE
    "degree celsius", "degrees celsius", "celsius", "dc", "c" -> UNIT.CELSIUS
    "degree fahrenheit", "degrees fahrenheit", "fahrenheit", "df", "f" -> UNIT.FAHRENHEIT
    "kelvin", "kelvins", "k" -> UNIT.KELVINS
    else -> UNIT.NULL
}