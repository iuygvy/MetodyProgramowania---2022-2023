// Bartosz Bugajski - 5
import java.util.Scanner;

/*
Zadanie polega na implementacji kolejki priorytetowej za pomoca max-kopca. Elementami kolejki sa liczby calkowite, ktore
moga sie powtarzac. Ilosc powtorzen elementu w kolejce decyduje o jego pozycji w niej (im wiecej danej elementu, tym wyzej
w kolejce sie on znajduje, a dla elementow o takiej samej liczbie powtorzen o kolejnosci decyduje wieksza wartosc tego
elementu.
W celu rozwiazania zadania stworzylem klase maxHeap, ktora jest tablicowym max-kopcem z elementami typu Node. Node to
klasa przechowujaca dwie dane typu int: wartosc elementu oraz liczbe jego powtorzen.
W kazdym zestawie na poczatku dostajemy maksymalna mozliwa liczbe wszystkich elementow (maxSize) oraz maksymalna mozliwa
liczbe roznych elementow (maxDiff). Na podstawie tych danych tworze tablice o rozmiarze maxDiff.
Przykladowo podczas dodawania elementu do tablicy (Insert), jesli taki sam element znajduje sie juz w tablicy, to tylko
zwiekszam key, czyli liczbe powtorzen tego elementu, oraz size, czyli calkowity rozmiar i wykonuje upHeap. Jesli takiego
elementu nie ma w tablicy oraz diff != maxDiff to moge dodac go na koniec, a nastepnie wykonac upHeap.
*/

class Node
{
    int info; // wartosc elementu
    int key; // liczba powtorzen elementu

    public Node(int i, int k) // konstruktor
    {
        info = i;
        key = k;
    }

    public void Display() // wyswietlenie elementu
    {
        System.out.printf(info + " " + key + " ");
    }
}

class maxHeap
{
    Node[] elements; // tablica elementow (kolejka)
    int size; // aktualny rozmiar
    int maxSize; // maksymalny rozmiar
    int diff; // aktualna liczba roznych elementow
    int maxDiff; // maksymalna liczba roznych elementow

    public maxHeap(int N, int P) // konstruktor
    {
        size = 0;
        diff = 0;
        maxDiff = N;
        maxSize = P;
        elements = new Node[maxDiff];
    }

    void Insert(int x) // wstawienie elementu o wartosci x do kolejki
    {
        if(size == maxSize) return; // jesli tablica pelna to nie dodaje
        for(int i = 0; i < diff; i++)
        {
            if(elements[i].info == x) // jesli element jest juz w tablicy to tylko zwiekszam key, size i wykonuje upHeap
            {
                elements[i].key++;
                upHeap(i);
                size++;
                return;
            }
        }
        // jesli elementu nie ma w tablicy to sprawdzam czy liczba roznych elementow == maksymalnej liczbie roznych
        // elementow, jesli tak, to nie moge dodac elementu
        if(diff == maxDiff) return;

        // zwiekszam rozmiar oraz dodaje element na koniec tablicy, a nastepnie wykonuje upHeap
        size++;
        elements[diff++] = new Node(x, 1);
        upHeap(diff-1);
    }

    int Compare(Node n1, Node n2) // porownanie elementow
    {
        if(n1.key > n2.key) return 1;
        if(n1.key < n2.key) return -1;
        if(n1.info > n2.info) return 1;
        if(n1.info < n2.info) return -1;
        return 0;
    }

    void swap(int i1, int i2) // zamiana miejscami w tablicy elementow pod indeksami i1, i2
    {
        Node temp = elements[i1];
        elements[i1] = elements[i2];
        elements[i2] = temp;
    }

    void deleteMax(int howMany) // usuniecie danej liczby maksymalnych elementow z tablicy
    {
        if(size == 0) // jesli tablica jest pusta to wypisujemy "0 0" i konczymy funkcje
        {
            System.out.println("0 0");
            return;
        }
        int first = elements[0].info; // zapisuje wartosc pierwszego elementu tablicy
        if(howMany >= elements[0].key)
        {
            // jesli tu trafilismy, to znaczy ze musimy usunac wszystkie maksymalne elementy tablicy, czylie nie tylko
            // zmniejszyc key, a po prostu wyrzucic cale elements[0]
            howMany = elements[0].key;
            elements[0] = elements[--diff];
            downHeap(0, diff);
        }
        else
        {
            // w innym wypadku tylko zmniejszamy key o zadana wartosc i wykonujemy downheap
            elements[0].key -= howMany;
            downHeap(0, diff);
        }
        size -= howMany;
        System.out.println(first + " " + howMany); // wypisujemy wartosc i liczbe usunietych elementow
    }

    void upHeap (int k) // przesiewanie elementu pod indeksem k w gore
    {
        int i = (k-1)/2; // indeks poprzednika
        Node tmp = elements[k];
        while (k > 0 && Compare(elements[i], tmp) < 0)
        {
            elements[k] = elements[i];
            k = i; // przenosimy element w dol
            i = (i-1)/2; // przechodzimy do poprzednika
        }
        elements[k] = tmp; // wstawiamy elements[k] na swoje miejsce
    }

    void downHeap (int k, int n) // przesiewanie elementu pod indeksem k w dol
    {
        int j;
        Node tmp = elements[k];
        while ( k < n / 2 )
        {
            j = 2 * k + 1; // indeks lewego nastepnika

            // jesli prawy nastepnik > lewy nastepnik, to wybieramy prawy
            if(j < n - 1 && Compare(elements[j], elements[j+1]) < 0) j++;

            if (Compare(tmp, elements[j]) >= 0) break; // warunek kopca spelniony

            // przesuwamy element do gory
            elements[k] = elements[j];
            k = j;
        }
        elements[k] = tmp; // wstawiamy elements[k] na swoje miejsce
    }

    void Display() // wyswietlenie calej kolejki
    {
        for(int i = 0; i < diff; i++)
        {
            elements[i].Display();
        }
    }

    void heapSort(int n)
    {
        // w klasycznym HeapSorcie najpierw musimy stworzyc kopiec, jednak tutaj nie jest to konieczne, bo kopiec juz mamy
        // wiec od razy przechodzimy do fazy 2, czyli usuwania z kopca
        while (n > 0)
        {
            swap (0, n-1); // zamiana miejscami
            downHeap (0, --n);
        }
        Display();
        System.out.println();
    }
}

public class Source
{
    public static Scanner sc = new Scanner(System.in);
    public static void main(String[] args)
    {
        int sets = sc.nextInt(); // liczba zestawow
        for(int i = 0; i < sets; i++)
        {
            int N = sc.nextInt(); // maksymalna liczba roznych elementow
            int P = sc.nextInt(); // maksymalna liczba wszystkich elementow
            maxHeap Q = new maxHeap(N, P); // tworze nowa kolejke
            boolean cont = true;
            while(cont)
            {
                String instr = sc.next(); // czytamy kolejne instrukcje
                switch(instr)
                {
                    case "i": // insert
                        int numOfElements = sc.nextInt();
                        for(int j = 0; j < numOfElements; j++)
                        {
                            Q.Insert(sc.nextInt());
                        }
                        break;
                    case "g": // getmax
                        Q.deleteMax(sc.nextInt());
                        break;
                    case "s": // sort
                        Q.heapSort(Q.diff);
                        cont = false;
                        break;
                    default:
                        break;
                }
            }
        }
    }
}

/*
INPUT:
5
5 10
i 5 1 1 1 1 2 3 4 5 6 g 5 s
3 10
g 100 i 1 1 g 10 i 4 1 2 3 4 s
5 15
i 8 1 2 3 4 4 4 4 4 g 3 i 4 1 1 1 1 g 2 i 3 6 7 8 s
2 100
i
1 2 3 1 2 3 1 2 3 2
1 2 3 1 2 3 1 2 3 2
1 2 3 1 2 3 1 2 3 2
1 2 3 1 2 3 1 2 3 2
1 2 3 1 2 3 1 2 3 2
g 30 i 5 5 5 5 5 5 s
1 10
i 1 2 3 4 5 g 5 i 1
1 1 1 1 1 1 1 1 1 1
1 1 1 1 1 1 1 1 1 s

OUTPUT:
1 4
2 1
0 0
1 1
1 1 2 1 3 1
4 3
1 2
2 1 3 1 6 1 4 2 1 3
2 1
5 5
2 1
1 1
*/