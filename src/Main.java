

class Main {
    public static void main(String[] args) {
        if(args[0].equals("PROD_1")) {
            Produtos.PRODUTO_1 = true;
        } else if(args[0].equals("PROD_2")){
            Produtos.PRODUTO_2 = true;
        } 
		View view = View.getInstance();
		Controller controller = new Controller(view);		
        Controller.startSession();
    }
}