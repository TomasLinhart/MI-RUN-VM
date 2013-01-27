Implementace virtual machine


************************************************************************************************************************
************************************************************************************************************************
** Doděláná rozšíření / změny dle požadavků z prvním odevzdání
************************************************************************************************************************
**
** Typ (integer vs pointer) je zakódován do posledního bitu uloženého čísla na stacku / heapě
**  - Při ukládání hodnoty je nastaven tento bit, poté je integer rozdělen do byte pole a vložen na heapu / stack.
**  - Při vybírání hodnoty je proces opačný.
**
***
**
** GC nepoužívá pomocnou mapu pro informace o přemapování pozic.
** Tato informace je ukládáná na speciální pozici v hlavičce objektu.
**  - Pokud je objekt naklonován na novou pozici v druhé polovině heapy, do hlavičky ve staré polovině heapy je přidána
**    nová adresa. Naklonovaný objekt má tuto informaci vynulovanou.
**  - Pokud narazíme na objekt, který je již přemapován do nové heapy, nalezneme jeho novou adresu v hlavičce objektu.
**
***
**
** Přidána podpora pro dědičnost a dynamic dispatch při lookupu metod.
**  - Při volání se bere v potaz jméno metody a počet parametrů.
**  - Konkrétní metoda je vybrána dle aktuálního typu (třídy) předaných argumentů do metody.
**  - Je vybrána metoda, jejiž typy argumentů se nejvíce shodují s těmi na zásobníku.
**  - Bere se zřetel na splnění požadavku na všechny argumenty metody.
**  - U objektových argumentů (ne Integer) je nejlepší pasující přímá shoda typu (třídy), poté následují nadtypy.
**
************************************************************************************************************************
************************************************************************************************************************


- Rozlišují se dva typy - int a pointer na objekt (vše, co není int), metoda navíc může vracet void.
-- Pointer s hodnotu 0 je považován za null
- Bytecode soubory s instrukcemi jsou pro lepší čitelnost nahrazeny xml reprezentací.
- Knihovní třídy jsou ve složce classes, jsou načteny automaticky.

Maven projekt:
 - spuštění testů: mvn clean test
 - spuštění VM: mvn clean compile exec:java -Dexec.args="4096 64 512 Knapsack.xml"
 -- očekává soubor data.txt se zadáním, vygeneruje soubor result.txt s řešením

- Výchozí je development maven profil, který vypisuje debug informace.
- Pro nastavení production profilu je třeba spustit s parametrem -Pproduction.
 
- Arguenty pro spuštění VM jsou (dle pořadí):
  heap size (bytes), max stack frames, stack frame size (bytes), scrappy class file, [next scrappy class files]

------------

Seznam instrukcí VM:

- ipush = vloží int na zásobník
- ppush = vloží pointer na zásobník
- syscall = volání systémové funkce
- new = vytvoří nový objekt na heap a vloží pointer na zásobník
- invokevirtual = volání virtuální metody na objektu
- getfield = vloží hodnotu fieldu objektu na zásobník
- setfield = nastaví hodnotu fieldu objektu na zásobník
- ireturn = načte hodnotu int ze zásobníku a uloží ji na zásobník předchozího stackframe
- preturn = načte hodnotu pointeru ze zásobníku a uloží ji na zásobník předchozího stackframe
- return = pouze se vrátí na předchozí stack frame
- iadd = součet dvou int
- isub = odečtení dvou int
- imul = násobení dvou int
- idiv = dělení dvou int
- imod = modulo dvou int
- ifeq = if == pro int
- ifge = if >= pro int
- ifgt = if > pro int
- ifle = if <= pro int
- iflt = if < pro int
- ifneq = if != pro int
- ifnotnull = if pointer != null
- ifnull = if pointer == null
- newarray = vytvoří nové pole specifikované velikosti na heap a vrací pointer na zásobník
- iload = vloží na zásobník hodnotu lokální proměnné (int)
- pload = vloží na zásobník hodnotu lokální proměnné (pointer)
- istore = nastaví hodnotu lokální proměnné (int)
- pstore = nastaví hodnotu lokální proměnné (pointer)
- jump = relativní skok na instrukci
- newstring = vytvoří nový string objekt na heap a vrací pointer na zásobník
- dup = duplikuje poslední hodnotu na zásobníku
- iand = int && int
- ior = int || int
- ineg = !int
- popvalue = vypustí hodnotu ze zásobníku
- vload = vloží na zásobník hodnotu lokální proměnné 
- vstore = nastaví hodnotu lokální proměnné
- vreturn = načte hodnotu ze zásobníku a uloží ji na zásobník předchozího stackframe

------------

Garbage Collector:

Použit Mark&Copy GC, spuštěn automaticky při nedostatku paměti na 1/2 heapy.
Projde heapu a zásobník jednou, rovnou kopíruje aktivní objekty a mění hodnoty ukazatelů.