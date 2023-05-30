import java.util.Random;
import java.util.concurrent.Semaphore;

public class Cliente extends Thread
{
	private int IDCliente;
	private static Semaphore sedieSemaforo;
	private Semaphore Mutex;
	
	public Cliente(int id, Semaphore sedie, Semaphore mutex) {
		this.IDCliente=id;
		this.sedieSemaforo=sedie;
		this.Mutex=mutex;
	}

	@Override
	public void run() {
		System.out.println("Il cliente: "+this.IDCliente+" è appena arrivato");
		
		try {
			if(sedieSemaforo.tryAcquire()) {
				System.out.println("Il cliente  "+this.IDCliente+" si è seduto");
				
				Mutex.acquire();
				System.out.println("Il cliente "+this.IDCliente+" ha completato il ritratto");
				sedieSemaforo.release();
				
			}
			else {
				System.out.println("Cliente: "+this.IDCliente+" se ne va via");
			}
		}catch(InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}
}
