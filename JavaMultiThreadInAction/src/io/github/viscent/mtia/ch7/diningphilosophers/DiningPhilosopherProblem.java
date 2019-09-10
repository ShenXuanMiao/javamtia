package io.github.viscent.mtia.ch7.diningphilosophers;

import io.github.viscent.mtia.util.Debug;

import java.lang.reflect.Constructor;

public class DiningPhilosopherProblem {

    public static void main(String[] args) throws Exception {
        int numOfPhilosopers;
        numOfPhilosopers = args.length > 0 ? Integer.valueOf(args[0]) : 2;
        // 创建筷子
        Chopstick[] chopsticks = new Chopstick[numOfPhilosopers];
        for (int i = 0; i < numOfPhilosopers; i++) {
            chopsticks[i] = new Chopstick(i);
        }

        String philosopherImplClassName = System.getProperty("x.philo.impl");
        if (null == philosopherImplClassName) {
            philosopherImplClassName = "DeadlockingPhilosopher";
        }
        Debug.info("Using %s as implementation.", philosopherImplClassName);
        for (int i = 0; i < numOfPhilosopers; i++) {
            // 创建哲学家
            createPhilosopher(philosopherImplClassName, i, chopsticks);
        }
    }

    private static void createPhilosopher(String philosopherImplClassName,
                                          int id, Chopstick[] chopsticks) throws Exception {

        int numOfPhilosopers = chopsticks.length;
        @SuppressWarnings("unchecked")
        Class<Philosopher> philosopherClass = (Class<Philosopher>) Class
                .forName(DiningPhilosopherProblem.class.getPackage().getName() + "."
                        + philosopherImplClassName);
        Constructor<Philosopher> constructor = philosopherClass.getConstructor(
                int.class, Chopstick.class, Chopstick.class);
        Philosopher philosopher = constructor.newInstance(id, chopsticks[id],
                chopsticks[(id + 1)
                        % numOfPhilosopers]);
        philosopher.start();
    }
}
