import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

class Procss {
    int id;
    int burstTime;
    int arrivalTime;
    int remainingTime;
    int waitingTime;
    int turnAroundTime;
    int completionTime;
    int responseTime; // Add response time

    Procss(int id, int burstTime, int arrivalTime) {
        this.id = id;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.waitingTime = 0;
        this.turnAroundTime = 0;
        this.completionTime = 0;
        this.responseTime = -1;
    }
}

public class SRTF {
    static void calculateTimes(Procss[] processes) {
        int n = processes.length;
        int currentTime = 0;
        int completedCount = 0;
        boolean[] completed = new boolean[n];

        while (completedCount < n) {
            int idx = -1;
            int minRemainingTime = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (!completed[i] && processes[i].arrivalTime <= currentTime && processes[i].remainingTime < minRemainingTime) {
                    minRemainingTime = processes[i].remainingTime;
                    idx = i;
                }
            }

            if (idx == -1) {

                currentTime++;
            } else {

                Procss process = processes[idx];

                if (process.responseTime == -1) {
                    process.responseTime = currentTime - process.arrivalTime;
                }

                process.remainingTime--;

                if (process.remainingTime == 0) {
                    process.completionTime = currentTime + 1;
                    process.turnAroundTime = process.completionTime - process.arrivalTime;
                    process.waitingTime = process.turnAroundTime - process.burstTime;
                    completed[idx] = true;
                    completedCount++;
                }
                currentTime++;
            }
        }
    }

    static void displayResults(Procss[] processes) {
        System.out.println("\nProcess\tArrival Time\tBurst Time\tWaiting Time\tTurnaround Time\tCompletion Time\tResponse Time");
        for (Procss process : processes) {
            System.out.println("P" + process.id + "\t\t" + process.arrivalTime + "\t\t\t\t" + process.burstTime + "\t\t\t\t" + process.waitingTime + "\t\t\t\t" + process.turnAroundTime + "\t\t\t\t" + process.completionTime + "\t\t\t\t" + process.responseTime);
        }

        double totalWaitingTime = 0, totalTurnAroundTime = 0, totalResponseTime = 0;
        for (Procss process : processes) {
            totalWaitingTime += process.waitingTime;
            totalTurnAroundTime += process.turnAroundTime;
            totalResponseTime += process.responseTime; // Add to total response time
        }

        System.out.println("\nAverage Waiting Time: " + (totalWaitingTime / processes.length));
        System.out.println("Average Turnaround Time: " + (totalTurnAroundTime / processes.length));
        System.out.println("Average Response Time: " + (totalResponseTime / processes.length));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();

        Procss[] processes = new Procss[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time for process P" + (i + 1) + ": ");
            int arrivalTime = sc.nextInt();
            System.out.print("Enter burst time for process P" + (i + 1) + ": ");
            int burstTime = sc.nextInt();
            processes[i] = new Procss(i + 1, burstTime, arrivalTime);
        }

        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));
        calculateTimes(processes);
        displayResults(processes);

        sc.close();
    }
}
