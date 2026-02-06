//Bartosz Bugajski - 5
import java.util.Scanner;

class StringStack
{
    int maxsize; // rozmiar tablicy zawierajacej stos
    String[] el; // tablica zawierajaca stos wyrazen
    int[] prio; // tablica zawierajaca stos priorytetow
    int t = -1; // indeks wierzcholka stosu
    public StringStack(int size)
    {
        maxsize = size;
        el = new String[maxsize];
        prio = new int[maxsize];
    }
    public void push(String c)
    {
        el[++t] = c; // zwiekszamy indeks wierzcholka i dodajemy element na stos
    }

    public boolean merge(char c) // funkcja laczaca wyrazenie/wyrazenia z operatorem
    {
        if(c != '!' && c != '~' && t < 1)
        {
            // jesli tu trafilismy to oznacza ze dostalismy operator dwuargumentowy, a na stosie mamy tylko jedno
            // wyrazenie, wiec mamy blad
            return true;
        }
        // jesli priorytet wyrazenie na szczycie stosu jest nizszy lub rowny (w tym przypadku na wejsciu musi byc
        // operator o lewostronnej lacznosci) od priorytetu operatora to bierzemy wyrazenie w nawias
        if(prio[t] < priority(c) || prio[t] == priority(c) && c != '!' && c != '~' && c != '^' && c != '=')
        {
            el[t] = '(' + el[t] + ')';
        }
        if(c != '!' && c != '~') // jesli operator jest dwuargumentowy to sprawdzamy jeszcze wyrazenie na pozycji t-1
        {
            if(prio[t-1] < priority(c)) // jesli jego priorytet jest mniejszy od priorytety operatora to rowniez bierzemy w nawias
            {
                el[t-1] = '(' + el[t-1] + ')';
            }
            // laczymy wyrazenie w jedno, zmieniamy priorytet na priorytet operatora i zmniejszamy rozmiar stosu
            el[t-1] = el[t-1] + c + el[t];
            prio[t-1] = priority(c);
            t--;
        }
        else
        {
            // jesli operator jednoargumentowy to tylko dopisujemy na poczatek wyrazenia na szczycie stosu i zmieniamy
            // priorytet na priorytet operatora
            el[t] = c + el[t];
            prio[t] = priority(c);
        }
        return false; // operacja sie powiodla
    }

    public int priority(char c) // metoda zwracajaca priorytet operatora
    {
        if(c == '=')
            return 0;
        else if(c == '|')
            return 1;
        else if(c == '&')
            return 2;
        else if(c == '?')
            return 3;
        else if(c == '<' || c == '>')
            return 4;
        else if(c == '+' || c == '-')
            return 5;
        else if(c == '*' || c == '/' || c == '%')
            return 6;
        else if(c == '^')
            return 7;
        else if(c == '!' || c == '~')
            return 8;
        return 9;
    }
}

class Stack
{
    int maxsize; // rozmiar tablicy zawierajacej stos
    char[] el; // tablica zawierajaca stos operatorow
    int[] prio; // tablica zawierajaca stos priorytetow
    int t = -1; // indeks wierzcholka stosu
    public Stack(int size)
    {
        maxsize = size;
        el = new char[maxsize];
        prio = new int[maxsize];
    }
    public void push(char c)
    {
        el[++t] = c; // zwiekszamy indeks wierzcholka i dodajemy na stos
        prio[t] = priority(c); // ustawiamy priorytet
    }
    public char pop()
    {
        return el[t--]; // zwracamy operator i zmniejszamy indeks wierzcholka
    }
    public int priority(char c) // metoda zwracajaca priorytet operatora
    {
        if(c == '=')
            return 0;
        else if(c == '|')
            return 1;
        else if(c == '&')
            return 2;
        else if(c == '?')
            return 3;
        else if(c == '<' || c == '>')
            return 4;
        else if(c == '+' || c == '-')
            return 5;
        else if(c == '*' || c == '/' || c == '%')
            return 6;
        else if(c == '^')
            return 7;
        else if(c == '!' || c == '~')
            return 8;
        return 9;
    }
}

public class Source
{
    public static Scanner sc = new Scanner(System.in);
    public static void main(String[] args)
    {
        int lines = sc.nextInt(); // wczytanie ilosci linii
        for(int i = 0; i < lines; i++)
        {
            String notation = sc.next(); // wczytanie notacji (INF lub ONP)
            String badexp = sc.nextLine(); // wczytanie wyrazenia (razem ze spacjami)
            String exp = ""; // wyrazenie ktore bedziemy analizowac
            for (int j = 0; j < badexp.length(); j++) // przechodzimy przez cale wyrazenie
            {
                // dodajemy tylko dozwolone znaki
                if(badexp.charAt(j) > 96 && badexp.charAt(j) < 123 || badexp.charAt(j) == '!' || badexp.charAt(j) == '~'
                        || badexp.charAt(j) == '^' || badexp.charAt(j) == '*' || badexp.charAt(j) == '/' || badexp.charAt(j) == '%'
                        || badexp.charAt(j) == '+' || badexp.charAt(j) == '-' || badexp.charAt(j) == '<' || badexp.charAt(j) == '>'
                        || badexp.charAt(j) == '?' || badexp.charAt(j) == '&' || badexp.charAt(j) == '|' || badexp.charAt(j) == '='
                        || (badexp.charAt(j) == '(' || badexp.charAt(j) == ')') && notation.charAt(0) == 'I')
                {
                    exp += badexp.charAt(j);
                }
            }
            boolean err = false; // zmienna mowiaca o tym czy wyrazenie wejsciowe jest poprawne
            if(notation.charAt(0) == 'I')
            {
                // jesli notacja to INF to sprawdzamy wyrazenie wedlug schematu podanego w zadaniu
                int st = 0; // aktualny stan
                int naw1 = 0; // aktualna liczba nawiasow otwierajacych
                int naw2 = 0; // aktualna liczba nawiasow zamykajacych
                for(int j = 0; j < exp.length(); j++)
                {
                    if(st == 0)
                    {
                        if(exp.charAt(j) > 96 && exp.charAt(j) < 123)
                        {
                            st = 1;
                        }
                        else if(exp.charAt(j) == '~' || exp.charAt(j) == '!')
                        {
                            st = 2;
                        }
                        else if(exp.charAt(j) == '(')
                        {
                            naw1++;
                        }
                        else
                        {
                            err = true;
                            break;
                        }
                    }
                    else if(st == 1)
                    {
                        if((exp.charAt(j) < 97 || exp.charAt(j) > 122) && exp.charAt(j) != '~' && exp.charAt(j) != '!'
                                && exp.charAt(j) != '(' && exp.charAt(j) != ')')
                        {
                            st = 0;
                        }
                        else if(exp.charAt(j) == ')')
                        {
                            naw2++;
                            if(naw2 > naw1) // jesli nawiasow zamykajacych jest wiecej niz otwierajacych to wyrazenie jest niepoprawne
                            {
                                err = true;
                                break;
                            }
                        }
                        else
                        {
                            err = true;
                            break;
                        }
                    }
                    else
                    {
                        if(exp.charAt(j) > 96 && exp.charAt(j) < 123)
                        {
                            st = 1;
                        }
                        else if(exp.charAt(j) == '(')
                        {
                            naw1++;
                            st = 0;
                        }
                        else if(exp.charAt(j) != '~' && exp.charAt(j) != '!')
                        {
                            err = true;
                            break;
                        }
                    }
                }
                // jesli po przejsciu przez wyrazenie stan != 1 lub liczba nawiasow sie nie zgadza to jest ono niepoprawne
                if(st != 1 || naw1 != naw2)
                {
                    err = true;
                }
            }
            if(err)
            {
                System.out.println("ONP: error");
            }
            else
            {
                String outp = ""; // output
                int k = 0;
                if(notation.charAt(0) == 'I') // z INF na ONP
                {
                    Stack st = new Stack(exp.length()); // tworzymy nowy stos (znakowy)

                    // dopoki nie przejdziemy przez cale wyrazenie i na stosie nic nie pozostanie...
                    while(k < exp.length() || st.t >= 0)
                    {
                        // jesli przeszlismy przez wyrazenie a na stosie cos jest to po prostu to zdejmujemy az na stosie
                        // nie pozostanie nic
                        if(k >= exp.length())
                        {
                            outp += st.pop();
                            outp += ' ';
                        }
                        // jesli znak to litera to dodajemy do outputu
                        else if(exp.charAt(k) > 96 && exp.charAt(k) < 123)
                        {
                            outp += exp.charAt(k);
                            outp += ' ';
                        }
                        // jesli znak to nawias otwierajacy to dodajemy go na stos
                        else if(exp.charAt(k) == '(')
                        {
                            st.push('(');
                        }
                        // jesli znak to nawias zamykajacy to zdejmujemy (i dodajemy do outputu) ze stosu wszystko az
                        // do napotkania nawiasu otwierajacego, ktorego tez zdejmujemy ale nie dodajemy go do outputu
                        else if(exp.charAt(k) == ')')
                        {
                            while(st.el[st.t] != '(')
                            {
                                outp += st.pop();
                                outp += ' ';
                            }
                            st.pop();
                        }
                        else
                        {
                            // jesli operator jest prawostronnie laczny to sciagamy ze stosu i dodajemy do outputu
                            // wszystkie operatory o priorytecie mniejszym niz sprawdzany
                            if(exp.charAt(k) == '=' || exp.charAt(k) == '^' || exp.charAt(k) == '!' || exp.charAt(k) == '~')
                            {
                                while(st.t >= 0 && st.priority(exp.charAt(k)) < st.prio[st.t] && st.el[st.t] != '(')
                                {
                                    outp += st.pop();
                                    outp += ' ';
                                }
                            }
                            // jesli operator jest lewostronnie laczny to sciagamy ze stosu i dodajemy do outputu
                            // wszystkie operatory o priorytecie mniejszym lub rownym niz sprawdzany
                            else
                            {
                                while(st.t >= 0 && st.priority(exp.charAt(k)) <= st.prio[st.t] && st.el[st.t] != '(')
                                {
                                    outp += st.pop();
                                    outp += ' ';
                                }
                            }
                            // nastepnie dodajemy na stos sprawdzany operator
                            st.push(exp.charAt(k));
                        }
                        k++;
                    }
                    System.out.println("ONP: " + outp);
                }
                else // z ONP na INF
                {
                    StringStack st = new StringStack(exp.length()); // tworzymy nowy stos (napisowy)
                    // przechodzimy przez wszystkie znaki
                    while(k < exp.length() || st.t > 0)
                    {
                        if(k >= exp.length())
                        {
                            // jesli tu trafimy, to znaczy ze przeszlismy przez wszystkie znaki a na stosie jest wiecej niz
                            // jeden element, to z kolei oznacza ze wyrazenie nie jest obliczalne, czyli jest niepoprawne
                            err = true;
                            break;
                        }
                        // jesli znak to litera, to dodajemy ja na stos
                        else if(exp.charAt(k) > 96 && exp.charAt(k) < 123)
                        {
                            st.push(String.valueOf(exp.charAt(k)));
                            st.prio[st.t] = 9;
                        }
                        // jesli znak to operator to laczymy go z wyrazeniem/wyrazeniami na stosie
                        else
                        {
                            if(st.merge(exp.charAt(k))) // wynik operacji 1 oznacza ze wyrazenie jest niepoprawne
                            {
                                err = true;
                                break;
                            }
                        }
                        k++;
                    }
                    if(!err)
                    {
                        for (int a = 0; a < st.el[0].length(); a++)
                        {
                            outp = outp + st.el[0].charAt(a) + ' ';
                        }
                        System.out.println("INF: " + outp);
                    }
                    else
                        System.out.println("INF: error");
                }
            }
        }
    }
}

/*
TESTY

Input:
20
INF: a+b    * ((c-d)/x) = ..,..,.,y
INF: a+b+c+d+e+f+g+h+i+j+k+l+m
INF: a^b^c^d^e
INF: a*((b-c)*d-b*(e+f))-b
INF: x=~a*b/c-d+e%~f
INF: a + () * b = z
INF: q*w+(e-r)/(t-y) = x+
INF: !!!!!!!(!!!(!!!(!!!!a)+b)+c)
INF: x
INF: (a+b)*c+(d-a)*(f-b)
ONP: xabcdefg++++++=
ONP: abcde^^^=
ONP: ab+*
ONP: xabc-d++=
ONP: xabcde^^===
ONP: a~b~~+c~/~d~*~e~^~^~
ONP: dfghjklwertyuiop+-*^/<>=+-*^/<>=
ONP: aaa~**~
ONP: abcdefgh ijklmnopr stuwxyz======    =================
ONP: a++   +++b++++c+

Output:
ONP: a b c d - x / * + y =
ONP: a b + c + d + e + f + g + h + i + j + k + l + m +
ONP: a b c d e ^ ^ ^ ^
ONP: a b c - d * b e f + * - * b -
ONP: x a ~ b * c / d - e f ~ % + =
ONP: error
ONP: error
ONP: a ! ! ! ! ! ! ! b + ! ! ! c + ! ! ! ! ! ! !
ONP: x
ONP: a b + c * d a - f b - * +
INF: x = a + ( b + ( c + ( d + ( e + ( f + g ) ) ) ) )
INF: a = b ^ c ^ d ^ e
INF: error
INF: x = a + ( b - c + d )
INF: x = a = b = c ^ d ^ e
INF: error
INF: error
INF: ~ ( a * ( a * ~ a ) )
INF: a = b = c = d = e = f = g = h = i = j = k = l = m = n = o = p = r = s = t = u = w = x = y = z
INF: error
 */