# Problema-delle-caricature

## Analisi:
  Il Problema dell'Artista che deve disegnare un ritratto per ogni Cliente necessità di:
  - `Thread Artista` il quale permetterà di creare i disegni per i Clienti.
  - `Thread Cliente` il quale gestirà il cliente facendolo sedere se ci sarà posto, oppure ritirandolo se dovrà aspettare molto.
  - `Semaforo a conteggio` il quale funge dalle 4 sedie dove aspettano i Clienti.
  - `Mutex` il quale dice quando il Cliente avrà la propria caricatura.

## Algoritmo
  Il programma è suddiviso in tre Classi:
  - La classe del `main`.
  - La classe dell'`Artista`.
  - La classe del `Cliente`.
  
### Artista
  L'Artista prevede che non ci sia un tempo preciso per fare le caricature, per questo è `Random`.
  Nel Thread dopo aver preso un posto delle sedie per farne la caricatura del Cliente aspetterà il tempo di fine lavoro.
  Solo dopo libererà la `sedia` per il prossimo cliente liberando anche il `completionMutex` il quale avvertirà il Cliente che la sua caricatura è pronta.
  ```Java
  public void run() {
		for(int i=1;i<=NUM_OF_ACTIONS;i++)
		{
			try {
				chairsSemaphore.acquire();
				System.out.println("L'artista inizia a disegnare");
				
				int tempoDiLavoro = new Random().nextInt(MAX_EXECUTION_TIME); 
				Thread.sleep(tempoDiLavoro*1000);
				
				System.out.println("Disegno Fatto");
				completionMutex.release();
				chairsSemaphore.release();
				
			}catch(InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}
  ```
  
  Attenzione che l'artista potrebbe continuare a disegnare anche se non ci saranno Clienti.
  
###  Cliente
  Il Cliente Aspetta che ci sia una sedia libera per potersi sedere.
  Se passa troppo tempo si ritira.
  Dopo essersi seduto aspetta che sia il suo turno per il disegno per merito del `completionMutex` il quale serve per avvertire quando il disegno è stato fatto.
  Completandosi cosi il processo del Cliente.
  ```Java
  public void run() {
		System.out.println("Il cliente: "+this.IDCliente+" è appena arrivato");
		
		try {
			if(chairsSemaphore.tryAcquire()) {
				System.out.println("Il cliente  "+this.IDCliente+" si siede");
				
				completionMutex.acquire();
				System.out.println("Il cliente "+this.IDCliente+" ha completato il ritratto");
				chairsSemaphore.release();
				
			}
			else {
				System.out.println("Cliente: "+this.IDCliente+" se ne va");
			}
		}catch(InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}
  ```
  
### Main 
  Il main semplicemente serve per generare l'Artist e i Clienti i quali saranno limitati a un massimo scelto.
  ```Java
  public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		final int NUM_CHAIRS = 4;
		final int NUM_OF_ACTIONS = 10;
	    Semaphore chairsSemaphore = new Semaphore(NUM_CHAIRS, true);
	    Semaphore completionMutex = new Semaphore(0, true);
	    Random random = new Random();

	    Artista artist=new Artista(chairsSemaphore,completionMutex,NUM_OF_ACTIONS);
	    artist.start();
	    
	    for (int i = 1; i <= NUM_OF_ACTIONS; i++) {
            Thread clientThread = new Thread(new Cliente(i,chairsSemaphore,completionMutex));
            clientThread.start();

            try {
                Thread.sleep(random.nextInt(2000)); // Intervallo di tempo casuale tra l'arrivo di un cliente e l'altro
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}
  ```
  
