## Projekt na zaliczenie - "Proste operacje bankowe"
### Według założeń aplikacja spełnia wymogi:
- Aplikacja z interfejsem tekstowym
- Pozwala wykonywać podstawowe operacje (depozyt, wypłata, transfer)
- Pozwala sprawdzać saldo konta
- Pozwala sprawdzać tranzakcje z wybranego przedziału czasowego
- Jeden klient ma dokładnie jedno konto
- Przechowuje informacje o klientach / użytkownich w plikach binarnych.

### Jak działa?
1) Przy uruchomieniu, aplikacja ładuje dane z pliku do klasy "Bank" gdzie przechowuje dane o klientach.
2) Jeżeli pliku nie ma, plik jest tworzony.
3) Jeżeli plik jest pusty, uzupełniony jest 3 przykładowymi klientami o numerach bankowych:
    - 00-0001
    - 00-0002
    - 00-0003
4) Następnie aplikacja prosi o podanie numeru konta w celu symulacji logowania (bez haseł dla uproszczenia)
5) Po zalogowaniu można wykonywać wszystkie operacje zawarte w założeniach projektowych
6) Po zakończeniu pracy można wyjść z aplikacji według instrukcji głównego menu, po czym następuje zapisanie danych do pliku i wyłączenie aplikacji.
