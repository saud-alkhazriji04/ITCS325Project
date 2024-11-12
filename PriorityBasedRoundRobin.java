import java.util.*;

public class PriorityBasedRoundRobin {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("enter: quantume time q in ms: ");
        int q = input.nextInt();
        System.out.println(q);

        System.out.println("enter: pid, AT, BT, and priority. enter 0 0 0 0 to terminate.");

        Map<Integer, Integer> burstMap = new HashMap<>();//burst time hashmap

        ArrayList<int[]> arrayLists = new ArrayList<>();

        boolean flag = true;
        while(flag) {
            int[] list = new int[4];
            for (int i=0; i<4; i++) {
                list[i] = input.nextInt();
            }
            // exit
            if (list[0] == 0 && list[1] == 0 && list[2] == 0 && list[3] == 0) {
                flag = false;
            } else { // cont
                arrayLists.add(list);
                burstMap.put(list[0], list[2]);//add burst time to a hashmap
            }
        }


        //the user finished entering values
        /*---------------------------------------------*/
        //convert to matrix
        int[][] newArr = arrayLists.toArray(new int[][]{});
        //sort matrix based on arrival time
        Arrays.sort(newArr, Comparator.comparingInt(row -> row[1]));

        Map<Integer, Integer> turnAround = new HashMap<>();//for the turnaround time
        Map<Integer, Integer> response = new HashMap<>();//for the response time
        Map<Integer, Integer> waiting = new HashMap<>();//for the waiting time


/*************************************************************************/
        CircularQueue<int[]> arrive = new CircularQueue<>();


        //add sorted(based on arrival time) process to the arrival queue
        for (int[] process : newArr) {
            arrive.offer(process);
        }

        int time = 0;
        List<int[]> exec = new ArrayList<>();//execution list
        System.out.println();
        System.out.println("Gantt's Chart:");
        System.out.print("0");
        while (!arrive.isEmpty() || !exec.isEmpty()) {
            //move arrived processes from arrive to exec
            while (!arrive.isEmpty() && arrive.peek()[1]<=time) {
                exec.add(arrive.poll());
            }
            exec.sort(Comparator.comparingInt(p -> p[3]));//sort based on priority

            //execute process with the highest priority and first arrival time
            if (!exec.isEmpty()) {

                int[] process = exec.get(0);
                if (!response.containsKey(process[0])) {
                    response.put(process[0], time); //add response time
                }

                boolean remove = false;
                if ((exec.size()>1 && exec.get(1)[3] != process[3]) || exec.size()==1) {//only process with that priority
                    System.out.print("-" +"p"+process[0]+"-"+(process[2]+time));
                    time += process[2];//fix later
                    remove = true;
                } else {
                    if (process[2] > q) {//if burst time is > quantum time
                        System.out.print("-"+"p"+process[0]+"-"+(time + q));
                        process[2] -= q;
                        time += q; //fix later
                        exec.remove(0);//move to the back so other processes of the same priority will be run
                        exec.add(process);

                    } else {//if burst time < quantum time
                        System.out.print("-"+"p"+process[0]+"-"+(time + process[2]));
                        time += process[2]; //fix later
                        remove = true;
                    }
                }
                //if process is finished count turnaround time && remove it from exec
                if (remove){
                    int tAT = time - process[1]; //Turn Around Time = Completion Time – Arrival Time
                    turnAround.put(process[0], tAT);//store the id and turn around time
                    int wt = tAT - burstMap.get(process[0]);//waiting time = Turn Around Time - Burst Time
                    waiting.put(process[0], wt); //store turn around time
                    exec.remove(0);
                }

            } else time++;
        }

        System.out.println();
        System.out.println();

        int avgTat = 0;
        int avgRt = 0;
        int avgWt = 0;
        int counter = 0;
        System.out.println("------------------------------------");
        System.out.println("pid:|TurnAround:|Response:| Waiting:");
        System.out.println("------------------------------------");

        for (int key : turnAround.keySet()) {
            avgTat += turnAround.get(key);
            avgRt += waiting.get(key);
            avgWt += waiting.get(key);
            counter++;
            System.out.print("p"+key+":\t|\t"+turnAround.get(key)+"\t");
            System.out.print("\t|\t" + response.get(key)+"\t");
            System.out.print("  |\t  "+waiting.get(key));
            System.out.println();
            System.out.println("------------------------------------");
        }
        System.out.println("avg:|\t" + avgTat/counter + "\t\t|\t" + avgRt/counter + "    |\t  " + avgWt/counter);
        System.out.println("------------------------------------");
        System.out.println();
        System.out.println("************ End Program ***********");


    }
}
