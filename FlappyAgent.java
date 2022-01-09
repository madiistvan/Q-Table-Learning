public class FlappyAgent {
    public static class StateDTO {
        public int birdPos;
        public int birdSpeed;
        public int objectType;
        public int objectDistance;
        public int objectHeight;

        public StateDTO(int birdPos, int birdSpeed, int objectType, int objectDistance, int objectHeight) {
            this.birdPos = birdPos;
            this.birdSpeed = birdSpeed;
            this.objectType = objectType;
            this.objectDistance = objectDistance;
            this.objectHeight = objectHeight;
        }

        @Override
        public String toString() {
            return "StateDTO{" +
                    "birdPos=" + birdPos +
                    ", birdSpeed=" + birdSpeed +
                    ", objectType=" + objectType +
                    ", objectDistance=" + objectDistance +
                    ", objectHeight=" + objectHeight +
                    '}';
        }
    }
    public static class QTable implements java.io.Serializable {
        public double[][][][][][] table;

        public QTable() {

        }

        public QTable(int[] stateSpaceSize, int actionDimension) {
            table = new double[stateSpaceSize[0]][stateSpaceSize[1]][stateSpaceSize[2]][stateSpaceSize[3]][stateSpaceSize[4]][actionDimension];
        }

        public double[] getActions(StateDTO state) {
            return table[state.birdPos][state.birdSpeed][state.objectType][state.objectDistance][state.objectHeight];
        }

        public QTable copy() {
            QTable res = new QTable();
            res.table = this.table.clone();
            return res;
        }
    }

    QTable qTable;
    int[] actionSpace;
    int nIterations;

    boolean test = false;

    public FlappyAgent(int[] observationSpaceSize, int[] actionSpace, int nIterations) {
        this.qTable = new QTable(observationSpaceSize,actionSpace.length);
        this.actionSpace = actionSpace;
        this.nIterations = nIterations;
    }

    public int step(StateDTO state) {
        return getAction(state);
    }

    public void epochEnd(int epochRewardSum) {

    }
    double learnRate = 0.14;
    double gamma = 0.23;
    public void learn(StateDTO oldState, int action, StateDTO newState, double reward) {
        var Q=qTable.table[oldState.birdPos][oldState.birdSpeed][oldState.objectType][oldState.objectDistance][oldState.objectHeight][action];
        Q = ((1-learnRate) * Q ) +(learnRate * (reward + gamma * getMaxQ(newState)));
        qTable.table[oldState.birdPos][oldState.birdSpeed][oldState.objectType][oldState.objectDistance][oldState.objectHeight][action] = Q;
    }
    public double getMaxQ(StateDTO newState){
        double[] actions = qTable.getActions(newState);
        double max = -1000.0;
        double currVal = 0;
        int index = 0;
        for (double a:actions) {
            currVal = qTable.table[newState.birdPos][newState.birdSpeed][newState.objectType][newState.objectDistance][newState.objectHeight][index];
            if(currVal > max) max = currVal;
            index++;
        }

        return max;
    }
    public int getAction(StateDTO newState){
        double[] actions = qTable.getActions(newState);
        double max = -1000.0;
        double currVal = 0;
        int index = 0;
        var maxIndex=0;
        for (double a:actions) {
            currVal = qTable.table[newState.birdPos][newState.birdSpeed][newState.objectType][newState.objectDistance][newState.objectHeight][index];
            if(currVal > max){ max = currVal;
                maxIndex = index;
            }
            index++;
        }

        return maxIndex;
    }
    public void trainEnd() {
        test = true;
    }
}
