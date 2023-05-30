import java.util.Random;
import java.util.concurrent.Semaphore;

public class DemoMain 
{
	public static void main(String[] args) 
	{
		final int numeroSedie = 4;
		final int numeroAzioni = 10;
	    Semaphore sedieSemaforo = new Semaphore(numeroSedie, true);
	    Semaphore Mutex = new Semaphore(0, true);
	    Random random = new Random();

	    Artista artist=new Artista(sedieSemaforo,Mutex,numeroAzioni);
	    artist.start();
	    
	    for (int i = 1; i <= numeroAzioni; i++) {
            Thread client = new Thread(new Cliente(i,sedieSemaforo,Mutex));
            client.start();

            try {
                Thread.sleep(random.nextInt(2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}

}
