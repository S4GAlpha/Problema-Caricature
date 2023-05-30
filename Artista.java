import java.util.Random;
import java.util.concurrent.Semaphore;

public class Artista extends Thread
{
	private static final int maxtTime = 5; 
	private static Semaphore sedieSemaforo;
	private Semaphore Mutex;
	private static int numeroAzioni;
	
	public Artista(Semaphore sedie, Semaphore mutex, int numeroAzioni) {
		this.sedieSemaforo=sedie;
		this.Mutex=mutex;
		this.numeroAzioni =numeroAzioni;
	}
	
	@Override
	public void run() {
		for(int i=1;i<=numeroAzioni;i++)
		{
			try {
				sedieSemaforo.acquire();
				System.out.println("L'artista inizia il disegno");
				
				int tempoDiLavoro = new Random().nextInt(maxtTime); 
				Thread.sleep(tempoDiLavoro*1000);
				
				System.out.println("Disegno Finito");
				Mutex.release();
				sedieSemaforo.release();
				
			}catch(InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

}
