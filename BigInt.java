package Main;

public class BigInt {

    final private int[] value;          //przechowuje wartość liczby bez znaku
    private boolean sign = true;        //zawiera wartość znaku liczy true jeśli dodatnia/false jeśli ujemna

    public BigInt(String bigIntValue) {
        String nonNegativeValue = bigIntValue;
        if (Character.getNumericValue(bigIntValue.charAt(0)) == -1) {
            nonNegativeValue = bigIntValue.replaceAll("-", "");
            this.sign = false;
        }
        value = new int[nonNegativeValue.length()];
        for (int position = 0; position < nonNegativeValue.length(); position++) {
            value[position] = Character.getNumericValue(nonNegativeValue.charAt(position));
        }
    }

    public BigInt(String bigIntValue, boolean sign) {
        this(bigIntValue);
        this.sign = sign;
    }

    public boolean getSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public int[] getAbsoluteValue() {
        return value;
    }

    public BigInt add(BigInt that) {

        String resultNumber= "";
        int[] number1 = this.getAbsoluteValue();
        int[] number2 = that.getAbsoluteValue();
        int tempIndex;

        //Sprawdzenie która liczba jest dłóższa i wyrównanie tablic do jednakowej długości
        if (number1.length > number2.length) {
            tempIndex = number1.length - 1;
            number2 = new int[number1.length];
            for (int i = that.getAbsoluteValue().length - 1; i >= 0; i--) {
                number2[tempIndex] = that.getAbsoluteValue()[i];
                tempIndex--;
            }
        } else if (number1.length < number2.length) {
            tempIndex = number2.length - 1;
            number1 = new int[number2.length];
            for (int i = this.getAbsoluteValue().length - 1; i >= 0; i--) {
                number1[tempIndex] = this.getAbsoluteValue()[i];
                tempIndex--;
            }
        }

        //Sprawdzenie znaków obu liczb, i ustalenie znaku wyniku oraz wybranie metody obliczenia

        //dla liczb o tych samych znakach ++ lub --
        if ((!this.sign && !that.getSign()) || (this.sign && that.getSign())) {
            int MEMORY = 0;
            String reverseResultNumber = "";

            for (int position = number1.length - 1; position >= 0; position--) {
                int resultAtPosition = number1[position] + number2[position] + MEMORY;
                MEMORY = 0;
                if (resultAtPosition >= 10) {
                    MEMORY = 1;
                    reverseResultNumber += (resultAtPosition - 10);
                    if (position == 0) {
                        reverseResultNumber += MEMORY;
                    }
                } else {
                    reverseResultNumber += resultAtPosition;
                }
            }
            StringBuilder temp = new StringBuilder(reverseResultNumber);
            resultNumber = temp.reverse().toString();

        //dla liczb +x i -y | x + (-y) => x - y
        } else if (this.sign && !that.getSign()) {
            BigInt temp = new BigInt(that.toString(), true);

            //gdy x >= y
            if (this.compareAbsoluteValue(that) >= 0) {
                return new BigInt(this.sub(temp).toString(),true);

            //gdy x < y
            } else if (this.compareAbsoluteValue(that) < 0) {
                return new BigInt(temp.sub(this).toString(), false);
            }

        //dla liczb -x i +y | -x + y => y - x
        } else if (!this.sign && that.getSign()) {
            BigInt temp = new BigInt(this.toString(), true);

            //gdy x<=y
            if (this.compareAbsoluteValue(that) <= 0) {
                return new BigInt(that.sub(temp).toString(), true);

            //gdy x>y
            } else if (this.compareAbsoluteValue(that) > 0) {
                return new BigInt(that.sub(temp).toString(), false);
            }
        }
        return new BigInt(resultNumber, this.getSign());
    }

    public BigInt sub(BigInt that) {

        String resultNumber= "";
        boolean resultSign = true;
        int[] number1 = this.getAbsoluteValue();
        int[] number2 = that.getAbsoluteValue();
        int tempIndex;

        //Sprawdzenie która liczba jest dłóższa i wyrównanie tablic do jednakowej długości
        if (number1.length > number2.length) {
            tempIndex = number1.length - 1;
            number2 = new int[number1.length];
            for (int i = that.getAbsoluteValue().length - 1; i >= 0; i--) {
                number2[tempIndex] = that.getAbsoluteValue()[i];
                tempIndex--;
            }
        } else if (number1.length < number2.length) {
            tempIndex = number2.length - 1;
            number1 = new int[number2.length];
            for (int i = this.getAbsoluteValue().length - 1; i >= 0; i--) {
                number1[tempIndex] = this.getAbsoluteValue()[i];
                tempIndex--;
            }
        }

        //Sprawdzenie znaków obu liczb, i ustalenie znaku wyniku oraz wybranie metody obliczenia

        //dla liczb +x i +y
        if (this.sign && that.getSign()) {

            //gdzie x >= y
            if (this.compareAbsoluteValue(that) >= 0) {
                int MEMORY = 0;
                String reverveResultNumber = "";

                for (int position = number1.length - 1; position >= 0; position--) {
                    int resultAtPosition = number1[position] - number2[position] - MEMORY;
                    MEMORY = 0;
                    if (resultAtPosition < 0) {
                        MEMORY = 1;
                        reverveResultNumber += resultAtPosition + 10;
                        if (position == 0) {
                            reverveResultNumber += MEMORY;
                        }
                    } else {
                        reverveResultNumber += resultAtPosition;
                    }
                }
                StringBuilder temp = new StringBuilder(reverveResultNumber);
                temp.reverse();
                boolean isZero = true;
                while (isZero) {
                    if (temp.length() > 1 && Character.getNumericValue(temp.charAt(0)) == 0) {
                        if (Character.getNumericValue(temp.charAt(0)) == 0) {
                            temp.delete(0, 1);
                        } else {
                            isZero = false;
                        }
                    } else {
                        isZero = false;
                    }
                }
                resultNumber = temp.toString();

            //gdzie x < y
            } else if (this.compareAbsoluteValue(that) < 0) {
                return new BigInt(that.sub(this).toString(), false);
            }

        //dla liczb -x i -y | -x-(-y) => -x+y
        } else if (!this.sign && !that.getSign()) {

            //gdy x>=y
            if (this.compareAbsoluteValue(that) > 0) {
                BigInt temp1 = new BigInt(this.toString(), true);
                BigInt temp2 = new BigInt(that.toString(), true);
                return new BigInt(temp1.sub(temp2).toString(), false);

            //gdy x<y
            } else if (this.compareAbsoluteValue(that) <= 0) {
                BigInt temp1 = new BigInt(this.toString(), true);
                BigInt temp2 = new BigInt(that.toString(), true);
                return new BigInt(temp2.sub(temp1).toString(), false);
            }

        //dla liczb -x i +y | -x - y
        } else if (!this.sign && that.getSign()) {
                BigInt temp = new BigInt(this.toString(), true);
                return new BigInt(temp.add(that).toString(), false);

        //dla liczb +x -y | x - (-y) => x + y
        } else if (this.sign && !that.getSign()) {
                BigInt temp = new BigInt(that.toString(), true);
                return new BigInt(this.add(temp).toString(), true);
        }

        return new BigInt(resultNumber, resultSign);
    }

    public BigInt mul(BigInt that) {

        //Inicjalizacja potrzebnych zmiennych
        boolean resultSignValue = resultSignForMultiplication(that);
        BigInt resultValue = new BigInt("0", true);
        int[] number1 = this.getAbsoluteValue();
        int[] number2 = that.getAbsoluteValue();
        String reverseResult = "";
        int MEMORY = 0;
        int addZero = 0;

        //Ustawienie krótszej liczby jako mnożnik
        if (number1.length > number2.length) {
            number1 = that.getAbsoluteValue();
            number2 = this.getAbsoluteValue();
        }

        //Mnożenie "pod kreską",przemnażanie liczby przez n-tą liczbę mnożnika, a następnie suma wyników
        for (int i = number2.length-1; i >= 0; i--) {
            reverseResult = "";
            MEMORY = 0;
            int n = number2[i];
            for (int j = number1.length-1; j >= 0; j--) {
                int m = number1[j];
                int nxmPlusMemory = (n * m) + MEMORY;
                if (nxmPlusMemory >= 10) {
                    MEMORY = nxmPlusMemory / 10;
                    nxmPlusMemory = nxmPlusMemory % 10;
                } else {
                    MEMORY = 0;
                }
                reverseResult += nxmPlusMemory;
                if (j == 0 && MEMORY != 0) {
                    reverseResult += MEMORY;
                }
            }
            //Dodanie odpowiedniej liczby zer na końcu wyniku (tu na poczatku, bo potem jest odwracany)
            for (int k = addZero; k > 0; k--) {
                reverseResult = "0" + reverseResult;
            }
            addZero += 1;

            //Odwrócenie otrzymanego wyniku
            StringBuilder correctResult = new StringBuilder(reverseResult).reverse();
            resultValue = resultValue.add(new BigInt(correctResult.toString(), true));
        }
        resultValue.setSign(resultSignValue);

        return resultValue;
    }

    public BigInt div(BigInt that) {

        BigInt division = null;
        try {
            division = divOrModulo(that, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return division;
    }

    public BigInt pow(BigInt power) throws Exception {

            if (!power.getSign()) {
                throw new Exception("Non negative number required");
            } else if (power.compareAbsoluteValue(new BigInt("99999")) > 0) {
                throw new Exception("The power is to big");
            }

        BigInt number = this;
        long pow = Long.parseLong(power.toString());

        //Wielokrotne mnożenie liczby przez samą siebie
        for (long i = 1; i < pow; i++) {
            number = number.mul(this);
        }

        //Sprawdzenie czy liczba była ujemna, jeśli tak to sprawdzenie czy potęga jest nieparzysta i ustawienie znaku wyniku
        if (!sign) {
            if (pow % 2 == 1) {
                number.setSign(false);
            } else
                number.setSign(true);
        }

        return number;
    }

    public BigInt pow(int power) {

        //Wywolanie metody pow(BigInt)
        BigInt result = null;
        try {
            result = this.pow(new BigInt(String.valueOf(power)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public BigInt modulo(BigInt that) {

        BigInt modulo = null;
        try {
            modulo = divOrModulo(that, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modulo;
    }

    public BigInt toBinaryBigInt() {

        return new BigInt(this.toBinaryString());
    }

    public String toBinaryString() {

        String reverseBinary = "";
        BigInt TWO = new BigInt("2");
        BigInt ZERO = new BigInt("0");
        BigInt tempNumber = new BigInt(this.toString(), true);

        while (tempNumber.compareAbsoluteValue(ZERO) != 0) {
            reverseBinary += tempNumber.modulo(TWO).toString();
            tempNumber = tempNumber.div(TWO);
        }
        if (reverseBinary.equals("")) {
            reverseBinary = "0";
        }
        String correctResult = new StringBuilder(reverseBinary).reverse().toString();

        return correctResult;
    }

    public String xorString(BigInt that) {

        //Konwersja liczb na ich reprezentację binarną
        BigInt thisInBinary = new BigInt(this.toBinaryString());
        BigInt thatInBinary = new BigInt(that.toBinaryString());

        int[] binaryNumber1 = thisInBinary.getAbsoluteValue();
        int[] binaryNumber2 = thatInBinary.getAbsoluteValue();

        //Wyrównanie wielkości obu tablic z liczbami w zapisie binarnym
        if (binaryNumber1.length > binaryNumber2.length) {
            binaryNumber2 = new int[binaryNumber1.length];
            int tempIndex = binaryNumber2.length-1;
            for (int i = thatInBinary.getAbsoluteValue().length-1; i >= 0; i--) {
                binaryNumber2[tempIndex] = thatInBinary.getAbsoluteValue()[i];
                tempIndex--;
            }
        } else  if (binaryNumber1.length < binaryNumber2.length) {
            int tempIndex = binaryNumber2.length-1;
            binaryNumber1 = new int[binaryNumber2.length];
            for (int i = thisInBinary.getAbsoluteValue().length - 1; i >= 0; i--) {
                binaryNumber1[tempIndex] = thisInBinary.getAbsoluteValue()[i];
                tempIndex--;
            }
        }

        //Wykonanie operacji xor
        String xorResult = "";
        for (int i = 0; i < binaryNumber1.length; i++) {
             xorResult += binaryNumber1[i]^binaryNumber2[i];
        }
        while (Character.getNumericValue(xorResult.charAt(0)) == 0) {
            xorResult = xorResult.substring(1);
        }

        return xorResult;
    }

    public BigInt xor(BigInt decimalValue) {

        //Obliczenie wyniky xor reprezentacji binarnej dwoch podanych liczb
        BigInt binaryNumber = new BigInt(this.xorString(decimalValue));

        return binaryNumber.binaryToDecimal();
    }

    public BigInt binaryToDecimal() {

        int[] binaryNumber = this.getAbsoluteValue();
        BigInt result = new BigInt("0");
        BigInt bitValue = new BigInt("1");

        //Wyliczenie liczby z zapisu binarnego
        for (int i = binaryNumber.length-1; i >= 0; i--) {
            if (binaryNumber[i] == 1) {
                result = result.add(bitValue);
            }
            bitValue = bitValue.mul(new BigInt("2"));
        }

        return result;
    }

    private BigInt divOrModulo(BigInt that, boolean modulo) throws Exception {

        //Jeśli dzielnik jest większy od dzielnej to wynik jest zero
        if (that.compareAbsoluteValue(this) > 0) {
            if (modulo) {
                return this;
            }
            return new BigInt("0", true);
        }

        //Wyjątek w przypadku dzielenia przez zero
        if (that.toString() == "0") {
            throw new Exception("Dzielenie przez zero");
        }

        //Zmienne potrzebne do obliczeń
        int lenghtDifference = this.getAbsoluteValue().length - that.getAbsoluteValue().length - 1;
        if (lenghtDifference < 0) {
            lenghtDifference = 0;
        }
        BigInt dividendNumber = new BigInt(this.toString(),true);
        BigInt divisionResult = new BigInt("0", true);


        for (int i = lenghtDifference; i >= 0; i--) {

            //Rozszerzenie dzielnika o dodatkowe zera w celu zmniejszenia liczby obliczeń
            String zeroString = "";
            for (int j = 0; j < lenghtDifference-1; j++) {
                zeroString += "0";
            }
            BigInt extendedDivisor = new BigInt(that.toString() + zeroString, true);

            long counter = 0;
            while (dividendNumber.compareAbsoluteValue(extendedDivisor) >= 0 && dividendNumber.getSign()) {
                dividendNumber = dividendNumber.sub(extendedDivisor);
                counter++;
            }
            lenghtDifference -= 1;
            String result = String.valueOf(counter) + zeroString;
            divisionResult = divisionResult.add(new BigInt(result, true));
        }

        if (modulo) {
            dividendNumber.setSign(this.getSign());
            return dividendNumber;
        } else {
            return new BigInt(divisionResult.toString(), resultSignForDivision(that));
        }
    }

    private int compareAbsoluteValue(BigInt that) {
        //Stworzenie ciagu znaków bez znaku
        String thisNumber = "";
        for (int v : value) {
            thisNumber += v;
        }
        String thatNumber = "";
        for (int v : that.getAbsoluteValue()) {
            thatNumber += v;
        }
        //Porównanie długoście, dłuższa liczba jest większa
        if (thisNumber.length() > thatNumber.length()) {
            return 1;
        } else if (thisNumber.length() < thatNumber.length()) {
            return -1;
        }
        //Jeśli obie są tej samej długości to porównywane są kolejno liczby
        return thisNumber.compareTo(thatNumber);
    }

    private boolean resultSignForMultiplication(BigInt that) {

        //Jeśli liczby są tego samego znaku, wynik będzie dodatni, w przeciwnym wypadku ujemny
        if ((this.getSign() && that.getSign()) || (!this.getSign() && !that.getSign())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean resultSignForDivision(BigInt that) {

        //Jeśli liczby są tego samego znaku, wynik będzie dodatni, w przeciwnym wypadku ujemny
        if ((this.getSign() && that.getSign()) || (!this.getSign() && !that.getSign())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String output = "";
        if (!sign) {
            output = "-";
        }
        for (int v : value) {
            output += v;
        }
        return output;
    }
}
