//Bartosz Bugajski - 5
import java.util.Scanner;

public class Source
{
    public static Scanner sc = new Scanner(System.in);
    public static void main(String[] args)
    {
        int sets = sc.nextInt(); // wczytanie ilosci zestawow
        for(int i = 0; i < sets; i++)
        {
            int nz = sc.nextInt(); // wczytanie numeru zestawu
            sc.next(); // wczytanie ciagu znakow " : "
            int w = sc.nextInt(); // wczytanie ilosci wierszy
            int k = sc.nextInt(); // wczytanie ilosci kolumn
            int[][] set = new int[w][k];
            for(int j = 0; j < w; j++) // wczytywanie tablicy wierszami
            {
                for(int l = 0; l < k; l++)
                {
                    set[j][l] = sc.nextInt();
                }
            }
            int msmax = 0; // maksymalna suma
            int windexbegin = 0; // indeks poczatkowego wiersza szukanego prostokata
            int windexend = 0; // indeks koncowego wiersza szukanego prostokata
            int kindexbegin = 0; // indeks poczatkowej kolumny szukanego prostokata
            int kindexend = 0; // indeks koncowej kolumny szukanego prostokata
            int maxcount = 0; // ilosc elementow w maksymalnej podtablicy
            boolean empty = true; // zmienna do sprawdzania czy tablica ma tylko elementy ujemne
            int count; // ilosc elementow w aktualnie sprawdzanej podtablicy
            /*
            Szukamy w macierzy prostokata, ktorego suma elementow bedzie najwieksza.
            Mozemy to zrobic poprzez stworzenie pomocniczej tablicy o rozmiarze rownym ilosci kolumn tablicy
            dwuwymiarowej. Nastepnie w kazdym zakresie wierszy top-down (w kolejnosci 1-1, 1-2, ..., 2-2, 2-3, ...),
            gdzie 0<=top<=down<w (w - ilosc wierszy tablicy dwuwymiarowej) zapisujemy do tablicy pomocniczej sumy
            elementow znajdujacych sie w poszczegolnych kolumnach tego prostokata. Posrod elementow tej tablicy, szukamy
            ciagu, ktorego suma elementow bedzie najwieksza (w ten sposob ograniczamy prostokat od lewej i prawej strony),
            a nastepnie porownujemy ja z wczesniej znaleziona maksymalna suma. Przechodzac w ten sposob po wszystkich
            zakresach wierszy i szukajac wsrod nich najwiekszego zakresu od lewej do prawej, sprawdzimy wszystkie mozliwe
            kombinacje.

            Skoro indeksy top, down, left, right maja tworzyc ciag leksykograficznie najmniejszy, to naturalnie chcemy,
            aby pierwszy element byl jak najmniejszy, dlatego przechodzimy po wierszach, a nie kolumnach.
            */
            for(int top = 0; top < w; top++)
            {
                int[] helper = new int[k]; // pomocnicza tablica
                for(int down = top; down < w; down++)
                {
                    for(int n = 0; n < k; n++)
                    {
                        helper[n] += set[down][n]; // zapisujemy sumy elementow w poszczegolnych kolumnach
                    }
                    int mscur = 0; // aktualna suma
                    int localindex = 0; // lokalny indeks ograniczajacy prostokat z lewej strony
                    for(int z = 0; z < k; z++) // szukamy ciagu elementow o najwiekszej sumie
                    {
                        mscur += helper[z];
                        // liczymy ilosc elementow podtablicy na wypadek jesli aktualna suma bylaby rowna maksymalnej
                        count = (z - localindex + 1) * (down - top + 1);
                        // jesli aktualna suma jest wieksza od maksymalnej lub
                        // sa one rowne oraz liczba elementow sprawdzanego prostokata jest mniejsza lub
                        // aktualna suma jest rowna 0 i jest to pierwszy nieujemny element tablicy (empty = true)
                        if(mscur > msmax || mscur == msmax && count < maxcount || empty && mscur == 0)
                        {
                            msmax = mscur; // nadpisujemy maksymalna sume
                            // ustawiamy indeksy potencjalnie maksymalnej podtablicy
                            windexbegin = top;
                            windexend = down;
                            kindexbegin = localindex;
                            kindexend = z;
                            maxcount = count; // nadpisujemy ilosc elementow potencjalnie maksymalnej podtablicy
                            empty = false; // ustawiamy empty na false, gdyz juz wiemy, ze jest ona niepusta
                        }
                        if(mscur <= 0)
                        {
                            mscur = 0; // zerujemy aktualna sume
                            localindex = z + 1; // jesli element tablicy pomocniczej nie jest wiekszy od 0, to idziemy dalej
                        }
                    }
                }
            }
            if(!empty)
            {
                System.out.println(nz + ": n = " + w + " m = "+ k + ", ms = " + msmax + ", mst = a[" + windexbegin +
                ".." + windexend + "][" + kindexbegin + ".." + kindexend + "]");
            }
            else
            {
                System.out.println(nz + ": n = " + w + " m = "+ k + ", ms = " + msmax + ", mst is empty");
            }
        }
    }
}


/*
Testy:

    Input:
    20
    1 : 4 4
    0 0 0 0
    0 0 0 0
    0 0 0 0
    0 0 0 0
    2 : 6 8
    7 -8 2 -5 6 -7 -8 5
    -4 6 -10 7 -8 -1 -9 -10
    7 1 3 -7 -9 2 9 6
    -3 -6 1 -7 2 0 -5 -7
    5 3 -1 -3 1 -9 -6 -8
    1 -6 -3 3 10 6 -2 -1
    3 : 2 8
    10 -8 -9 -3 3 -10 8 5
    -9 5 -1 -3 7 2 -3 -8
    4 : 5 5
    6 2 -9 -4 -1
    8 3 6 -2 4
    -9 3 -1 -8 -6
    5 9 1 0 3
    8 4 -8 9 10
    5 : 6 1
    -10
    -5
    0
    -10
    3
    3
    6 : 2 8
    -1 7 7 -7 8 9 -3 7
    7 -6 5 -2 10 3 10 -2
    7 : 1 1
    9
    8 : 5 8
    5 -4 10 -9 8 1 10 -2
    -1 6 -9 -3 9 1 -6 5
    7 7 -4 -8 -8 6 -8 -7
    -8 -3 7 6 -9 -2 9 9
    3 10 6 10 1 1 6 -5
    9 : 1 7
    -2 -1 5 3 -5 -8 6
    10 : 6 8
    0 3 5 -6 -7 -1 5 4
    -7 -9 9 -7 -2 -7 -5 -10
    0 -3 2 -6 -5 -5 10 -5
    -9 -4 -4 -7 8 4 1 -9
    0 -4 1 6 -1 -10 -7 -5
    5 6 1 -2 -8 6 -8 0
    11 : 5 5
    4 -6 -1 -3 6
    5 -9 3 -4 -8
    -8 5 -3 3 -9
    -3 4 -5 -6 -4
    9 0 -6 2 -2
    12 : 1 5
    -1 8 -10 3 -9
    13 : 4 1
    6
    6
    -5
    -2
    14 : 1 8
    -4 6 -5 -4 9 6 -4 -7
    15 : 2 6
    -6 -1 -5 -3 -8 -1
    -9 -2 -3 -8 -3 -8
    16 : 8 1
    -1
    -4
    -1
    9
    -10
    9
    -4
    -10
    17 : 4 8
    -9 -3 -5 -4 -6 5 -5 -1
    1 -1 -6 2 2 8 8 8
    5 7 -6 9 -8 6 -7 0
    -4 -7 10 10 -4 7 -5 -9
    18 : 8 4
    4 6 6 0
    8 -7 3 -1
    2 -7 -3 -5
    -9 2 3 1
    0 -2 -5 10
    -5 3 -4 6
    2 10 -2 9
    -7 -2 4 3
    19 : 3 1
    8
    -1
    0
    20 : 4 4
    2 1 -1 4
    0 2 0 -6
    -4 -5 10 3
    -6 8 1 3

    Output:
    1: n = 4 m = 4, ms = 0, mst = a[0..0][0..0]
    2: n = 6 m = 8, ms = 19, mst = a[5..5][3..5]
    3: n = 2 m = 8, ms = 13, mst = a[0..0][6..7]
    4: n = 5 m = 5, ms = 41, mst = a[3..4][0..4]
    5: n = 6 m = 1, ms = 6, mst = a[4..5][0..0]
    6: n = 2 m = 8, ms = 52, mst = a[0..1][0..7]
    7: n = 1 m = 1, ms = 9, mst = a[0..0][0..0]
    8: n = 5 m = 8, ms = 47, mst = a[0..4][0..6]
    9: n = 1 m = 7, ms = 8, mst = a[0..0][2..3]
    10: n = 6 m = 8, ms = 16, mst = a[0..2][2..2]
    11: n = 5 m = 5, ms = 10, mst = a[3..4][0..1]
    12: n = 1 m = 5, ms = 8, mst = a[0..0][1..1]
    13: n = 4 m = 1, ms = 12, mst = a[0..1][0..0]
    14: n = 1 m = 8, ms = 15, mst = a[0..0][4..5]
    15: n = 2 m = 6, ms = 0, mst is empty
    16: n = 8 m = 1, ms = 9, mst = a[3..3][0..0]
    17: n = 4 m = 8, ms = 32, mst = a[1..3][3..5]
    18: n = 8 m = 4, ms = 36, mst = a[3..7][1..3]
    19: n = 3 m = 1, ms = 8, mst = a[0..0][0..0]
    20: n = 4 m = 4, ms = 20, mst = a[2..3][1..3]
 */