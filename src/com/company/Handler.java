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
    }

    public int getMinTimeThread(){
        double min = threadArrayList.get(0).getTimeFreeAll();
        int num = 0;
        for(int i =1; i < 4; i++){
            if(threadArrayList.get(i).getTimeFreeAll() < min){
                min = threadArrayList.get(i).getTimeFreeAll();
                num = i;
            }
        }
        return num;
    }

    public ArrayList<Integer> getFreeThreds(){
        // Stream<Boolean> booleanStream = threadArrayList.stream().filter(Thread::isFree).map(Thread::isFree);
        ArrayList<Integer> free = new ArrayList<>();

        for(int i =0; i < 4; i++){
            if(threadArrayList.get(i).isFree()){
                free.add(i);
            }
        }
        return free;
    }

    public void addRequests(Request request){
        double timeService = request.getTime();
       // int threadNum = valuesGenerator.discoverFate(4);
        double timeIncoming = valuesGenerator.getExpY();

        doStaff(timeIncoming);

        currentTime += timeIncoming;

        ArrayList<Integer> freeThreads = getFreeThreds();
        System.out.printf("\n[%.2f] Поступила " + request.getName() + ". время поступления: " + timeIncoming
                + " время обслуживания: " + request.getTime() +"\n", currentTime);


        if (freeThreads.isEmpty()){
            int min = getMinTimeThread();

            System.out.println("!!!нет свободных каналов. " + request.getName() + " остается ожидать. Обслужится каналом " + min
            + " через: " + (threadArrayList.get(min).getTimeFreeAll() - currentTime) + " минут");

            requestsListinThread.get(min).add(request);
            //requestArrayList.add(request);

            timeOjidaniya += threadArrayList.get(min).getTime() - currentTime;
            threadArrayList.get(min).setTimeFreeAll(timeService);

          //  System.out.println("канал освободится: " + (threadArrayList.get(min).getTimeFreeAll()));


        }
        else {
            int threadNum = valuesGenerator.discoverFate(freeThreads.size());
            // System.out.println(" sda " + freeThreads.size() + " " + threadNum + " " + freeThreads.get(threadNum));

            threadArrayList.get(freeThreads.get(threadNum)).serviceRequest(request, currentTime + timeService);
            System.out.println(" потоком №" + freeThreads.get(threadNum));
        }

//        if(threadArrayList.get(threadNum).isFree()){
//            threadArrayList.get(threadNum).serviceRequest(request, currentTime + timeService);
//            //requestsListinThread.get(threadNum).remove(0);
//        }else{
//            //requestsListinThread.get(threadNum).add(request);
//            requestArrayList.add(request);
//            System.out.println("------заявка " + request.getName() + " осталась ожидать");
//            timeOjidaniya += threadArrayList.get(threadNum).getTime() - currentTime;
//        }

        //System.out.println("\n--------------------------------------------\n");
    }

    public void doStaff(double timeIncoming){


        numOfIter++;
        for(int i = 0; i < 4; i++){

            queueLength += requestsListinThread.get(i).size();

            if(currentTime + timeIncoming >= threadArrayList.get(i).getTime() && threadArrayList.get(i).getTime() >= currentTime
                    && !threadArrayList.get(i).isFree()){

                System.out.printf("\n[%.2f] Поток № %d освободился\n", threadArrayList.get(i).getTime(), i);
                threadArrayList.get(i).setFree(true);
                if(!requestsListinThread.get(i).isEmpty()){
                    System.out.printf("[%.2f] ", threadArrayList.get(i).getTime());

                    threadArrayList.get(i).serviceRequest(requestsListinThread.get(i).get(0),
                            currentTime + timeIncoming + requestsListinThread.get(i).get(0).getTime());
                    System.out.println(" потоком №" + i);
                    requestsListinThread.get(i).remove(0);
                    //System.out.println("//Заявка удалена из очереди");
                }
            }
        }

    }

    public void doRemaining(){
        double t = 0;
        while(!requestsListinThread.get(0).isEmpty() || !requestsListinThread.get(1).isEmpty()
                || !requestsListinThread.get(2).isEmpty() || !requestsListinThread.get(3).isEmpty()) {
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


    public double getQueueLength() {
        return (double)numOfIter / queueLength;
    }

    public void setQueueLength(int queueLength) {
        this.queueLength = queueLength;
    }
}
