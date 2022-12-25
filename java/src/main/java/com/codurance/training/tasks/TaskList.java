package com.codurance.training.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static com.codurance.training.tasks.Command.COMMANDS;

public final class TaskList implements Runnable {
    private static final String ADD_PROJECT_SUBCOMMAND = "project";
    private static final String ADD_TASK_SUBCOMMAND = "task";


        private final Map<String, List<Task>> tasks = new LinkedHashMap<>();
        private final BufferedReader in;
        private final PrintWriter out;
        private Integer lastId = 0;

        public static void main(String[] args){
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(System.out);
            new TaskList(in, out).run();
        }

        public TaskList(BufferedReader in, PrintWriter out) {
            this.in = in;
            this.out = out;
        }

        public void run() {
            while (true) {
                out.print("> ");
                out.flush();
                String commandLine = null;
                try {
                    commandLine = in.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Command command = COMMANDS.get(commandLine.split(" ")[0]);

                if (command == null) {
                    error(commandLine);
                } else {
                    switch (command) {
                        case QUIT:
                            return;
                        case SHOW:
                            show();
                            break;
                        case ADD:
                            add(commandLine);
                            break;
                        case CHECK:
                            check(commandLine.split(" ")[1]);
                            break;
                        case UNCHECK:
                            uncheck(commandLine.split(" ")[1]);
                            break;
                        case HELP:
                            help();
                            break;
                    }
                }
            }
        }

        private void add(String commandLine) {
            String[] subcommandAndRest = commandLine.split(" ", 3);
            String subcommand = subcommandAndRest[1];
            if (ADD_PROJECT_SUBCOMMAND.equals(subcommand)) {
                addProject(subcommandAndRest[2]);
            } else if (ADD_TASK_SUBCOMMAND.equals(subcommand)) {
                String[] projectTask = subcommandAndRest[2].split(" ", 2);
                addTask(projectTask[0], projectTask[1]);
            }
        }

        private void addProject(String name) {
            tasks.put(name, new ArrayList<>());
        }

        private void addTask(String project, String description) {
            List<Task> projectTasks = tasks.get(project);
            if (projectTasks == null) {
                out.printf("Could not find a project with the name \"%s\".", project);
                out.println();
                return;
            }
            projectTasks.add(new Task(nextId(), description, false));
        }

        private void check(String idString) {
            setDone(idString, Boolean.TRUE);
        }

        private void uncheck(String idString) {
            setDone(idString, false);
        }

        private void setDone(String idString, Boolean done) {
            int id = Integer.parseInt(idString);
            for (Map.Entry<String, List<Task>> project : tasks.entrySet()) {
                for (Task task : project.getValue()) {
                    if (task.getId() == id) {
                        task.setDone(done);
                        return;
                    }
                }
            }
            out.printf("Could not find a task with an ID of %d.", id);
            out.println();
        }

        private void show() {
            for (Map.Entry<String, List<Task>> project : tasks.entrySet()) {
                out.println(project.getKey());
                for (Task task : project.getValue()) {
                    out.printf("    [%c] %d: %s%n", (task.isDone() ? 'x' : ' '), task.getId(), task.getDescription());
                }
                out.println();
            }
        }

        private void help() {
            out.println("Commands:");
            out.println("  show");
            out.println("  add project <project name>");
            out.println("  add task <project name> <task description>");
            out.println("  check <task ID>");
            out.println("  uncheck <task ID>");
            out.println("  help");
            out.println("  quit");
        }

        private void error(String command) {
            out.printf("Unrecognized command: %s", command);
            out.println();
        }

        private Integer nextId() {
            return ++lastId;
        }
    }




