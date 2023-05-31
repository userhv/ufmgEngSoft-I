

class Main {
    public static void main(String[] args) {
		View view = View.getInstance();
		Controller controller = new Controller(view);		
        Controller.startSession();
    }
}