Problema delle caricature
Analisi:
Il Problema dell'Artista che deve disegnare un ritratto per ogni Cliente richiede la gestione di diverse entità e risorse. In particolare, abbiamo:

Un Thread Artista che si occupa di creare i disegni per i clienti.
Un Thread Cliente che gestisce l'arrivo dei clienti, consentendo loro di sedersi se ci sono posti disponibili o di aspettare se non ci sono sedie disponibili.
Un Semaforo a conteggio che funge da contatore per le sedie disponibili, poiché solo un numero limitato di clienti può essere seduto contemporaneamente.
Un Mutex che viene utilizzato per segnalare al cliente quando il suo ritratto è pronto.
Algoritmo
Il programma è strutturato utilizzando tre classi principali:

La classe principale (Main) che funge da punto di ingresso del programma.
La classe Artista, che rappresenta il thread dell'artista.
La classe Cliente, che rappresenta il thread del cliente.
Artista
L'artista inizia disegnando i ritratti dei clienti, senza un tempo preciso per completare ciascun disegno. Il tempo di esecuzione viene generato casualmente. Dopo aver preso una sedia (risorsa condivisa) per iniziare il disegno, l'artista attende il tempo necessario per completare il ritratto. Successivamente, rilascia la sedia per il prossimo cliente e segnala al cliente che il ritratto è pronto utilizzando il mutex completionMutex.

java
Copy code
public void run() {
    for (int i = 1; i <= NUM_OF_ACTIONS; i++) {
        try {
            chairsSemaphore.acquire();
            System.out.println("L'artista inizia a disegnare");
            int tempoDiLavoro = new Random().nextInt(MAX_EXECUTION_TIME); 
            Thread.sleep(tempoDiLavoro * 1000);
            System.out.println("Disegno Fatto");
            completionMutex.release();
            chairsSemaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}

È importante notare che l'artista può continuare a disegnare anche se non ci sono clienti.

Cliente
Il thread del cliente gestisce l'arrivo dei clienti. Un cliente appena arrivato cerca di trovare una sedia disponibile per sedersi. Se una sedia è disponibile, il cliente si siede e attende che il suo ritratto venga completato utilizzando il mutex completionMutex. Una volta completato il ritratto, il cliente lascia la sedia per altri clienti.

java
Copy code
public void run() {
    System.out.println("Il cliente: " + this.IDCliente + " è appena arrivato");
    try {
        if (chairsSemaphore.tryAcquire()) {
            System.out.println("Il cliente " + this.IDCliente + " si siede");
            completionMutex.acquire();
            System.out.println("Il cliente " + this.IDCliente + " ha completato il ritratto");
            chairsSemaphore.release();
        } else {
            System.out.println("Cliente: " + this.IDCliente + " se ne va");
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
        Thread.currentThread().interrupt();
    }
}

Main
Il metodo main è il punto di ingresso del programma. Qui vengono create le istanze dell'artista e dei clienti. È possibile specificare il numero massimo di azioni (ad esempio, il numero massimo di clienti o di ritratti da realizzare). Le sedie disponibili per i clienti vengono gestite utilizzando un semaforo a conteggio (chairsSemaphore), mentre il mutex completionMutex viene utilizzato per sincronizzare il completamento del ritratto tra l'artista e i clienti.

java
Copy code
public static void main(String[] args) {
    final int NUM_CHAIRS = 4;
    final int NUM_OF_ACTIONS = 10;
    Semaphore chairsSemaphore = new Semaphore(NUM_CHAIRS, true);
    Semaphore completionMutex = new Semaphore(0, true);
    Random random = new Random();
    Artista artist = new Artista(chairsSemaphore, completionMutex, NUM_OF_ACTIONS);
    artist.start();
    for (int i = 1; i <= NUM_OF_ACTIONS; i++) {
        Thread clientThread = new Thread(new Cliente(i, chairsSemaphore, completionMutex));
        clientThread.start();
        try {
            Thread.sleep(random.nextInt(2000)); // Intervallo di tempo casuale tra l'arrivo di un cliente e l'altro
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

In questo esempio, il main crea un artista e diversi clienti che si alternano per ottenere una sedia e attendere il completamento del proprio ritratto.

Si noti che questa è solo una spiegazione generale dell'algoritmo, basata sul codice fornito. Potrebbero esserci ulteriori dettagli o requisiti specifici che non sono stati menzionati.




