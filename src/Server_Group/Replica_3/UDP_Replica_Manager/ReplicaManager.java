package Server_Group.Replica_3.UDP_Replica_Manager;

/**
 * Created by Mahdiye on 8/1/2016.
 */
public class ReplicaManager {

    public static void main(String[] args) {
        System.out.println("Front End Listener read and waiting");
        new Thread(new Front_End_Listener_Thread()).start();
        System.out.println("Broad Cast Listener read and waiting");
        new Thread(new Broad_Cast_Listener_Thread()).start();

        FailureDetection failureDetector = new FailureDetection();

    }

}
