import Front_End.Front_End_Server;
import Request_ID_Dispatcher.Request_ID_Dispatcher;
import Server_Group.Replica_1.Record_ID_Dispatcher.Record_ID_Dispatcher;
import Server_Group.Replica_1.Server_DDO.Start_DDO_Server;
import Server_Group.Replica_1.Server_LVL.Start_LVL_Server;
import Server_Group.Replica_1.Server_MTL.Start_MTL_Server;
import Server_Group.Replica_1.UDP_Replica_Manager.Replica_Manager;

public class start_run {
	
	static Thread front_end_server = new Thread(){
		@Override
		public void run() {
			Front_End_Server.main(null);
		}
	};
	
	static Thread request_id_dispatcher = new Thread(){
		@Override
		public void run() {
			Request_ID_Dispatcher.main(null);
		}
	};
	
	static Thread reocrd_id_dispatcher = new Thread(){
		public void run() {
			Record_ID_Dispatcher.main(null);
		};
	};
	
	static Thread start_host_1_mtl = new Thread(){
		public void run() {
			Start_MTL_Server.main(null);
		};
	};
	
	static Thread start_host_1_lvl = new Thread(){
		public void run() {
			Start_LVL_Server.main(null);
		};
	};
	
	static Thread start_host_1_ddo = new Thread(){
		public void run() {
			Start_DDO_Server.main(null);
		};
	};
	
	static Thread host_1_replica_manager = new Thread(){
		public void run() {
			Replica_Manager.main(null);
		};
	};
	
	public static void main(String[] args) {
		try {
			front_end_server.start();
			Thread.sleep(1000);
			request_id_dispatcher.start();
			Thread.sleep(1000);
			reocrd_id_dispatcher.start();
			Thread.sleep(1000);
			start_host_1_mtl.start();
			Thread.sleep(1000);
			start_host_1_lvl.start();
			Thread.sleep(1000);
			start_host_1_ddo.start();
			Thread.sleep(1000);
			host_1_replica_manager.start();
			Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
}
