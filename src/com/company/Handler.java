package com.company;

import java.util.ArrayList;

public class Handler {
    private ValuesGenerator valuesGenerator = new ValuesGenerator();
    private ArrayList<Thread> threadArrayList = new ArrayList<>();
    private ArrayList<Request> requestArrayList = new ArrayList<>();
    private ArrayList<ArrayList<Request>> requestsListinThread = new ArrayList<>();
    private double currentTime = 0;
    private int queueLength = 0;
    private int numOfIter = 0;
    private double timeOjidaniya = 0;


    public double getTimeOjidaniya() {
        return timeOjidaniya;
    }

    public void setTimeOjidaniya(double timeOjidaniya) {
        this.timeOjidaniya = timeOjidaniya;
    }

    public Handler(){
        threadArrayList.add(new Thread());
        threadArrayList.add(new Thread());
        threadArrayList.add(new Thread());
        threadArrayList.add(new Thread());

        System.out.println(threadArrayList.size());

        requestsListinThread.add(new ArrayList<>());
        requestsListinThread.add(new ArrayList<>());
        requestsListinThread.add(new ArrayList<>());
        requestsListinThread.add(new ArrayList<>());
        requestsListinThread.add(new ArrayList<>());

    }

    public void addRequests(Request request){
        double timeService = request.getTime();
        int threadNum = valuesGenerator.discoverFate(4);
        double timeIncoming = valuesGenerator.getUniformY();

        doStaff(timeIncoming);

        currentTime += timeIncoming;

        System.out.printf("\n[%.2f] Поступила " + request.getName() + ". Выбран поток №" + threadNum
                + " время поступления: " + timeIncoming + " вемя обслуживания: " + request.getTime() +"\n", currentTime);

        if(threadArrayList.get(threadNum).isFree()){
            threadArrayList.get(threadNum).serviceRequest(request, currentTime + timeService);
            //requestsListinThread.get(threadNum).remove(0);
        }else{
            //requestsListinThread.get(threadNum).add(request);
            requestArrayList.add(request);
            System.out.println("------заявка " + request.getName() + " осталась ожидать");
            timeOjidaniya += threadArrayList.get(threadNum).getTime() - currentTime;
        }

        //System.out.println("\n--------------------------------------------\n");
    }

    public void doStaff(double timeIncoming){

        for(int i = 0; i < 4; i++){

            queueLength += requestsListinThread.get(i).size();

            if(currentTime + timeIncoming >= threadArrayList.get(i).getTime() && threadArrayList.get(i).getTime() >= currentTime
                    && !threadArrayList.get(i).isFree()){

                System.out.printf("\n[%.2f] Поток № %d освободился\n", threadArrayList.get(i).getTime(), i);
                threadArrayList.get(i).setFree(true);
                if(!requestArrayList.isEmpty()){
                    System.out.printf("[%.2f] ", threadArrayList.get(i).getTime());

                    threadArrayList.get(i).serviceRequest(requestArrayList.get(0),
                            currentTime + timeIncoming + requestArrayList.get(0).getTime());
                    System.out.println(" потоком №" + i);
                   requestArrayList.remove(0);
                    //System.out.println("//Заявка удалена из очереди");
                }
            }
        }
        numOfIter++;
    }

    public void doRemaining(){
        double t = 0;
        while(!requestArrayList.isEmpty()) {
            doStaff(t++);
        }
    }

    public void printFreeThreads(){
        System.out.println("Потоки: ");
        for(Thread thread: threadArrayList){
            System.out.println("Поток " + thread.isFree());
        }
    }

    public void printRemaining(){
        System.out.println("Remaining:");
        for(ArrayList<Request> requests:requestsListinThread){
            for(Request request: requests){
                System.out.println(request.getName());
            }
        }
    }

    public void addThread(Thread thread){
        threadArrayList.add(thread);
    }


    public int getQueueLength() {
        return numOfIter / queueLength;
    }

    public void setQueueLength(int queueLength) {
        this.queueLength = queueLength;
    }
}
