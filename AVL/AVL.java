import java.util.ArrayList;

public class AVL<T> {

    private static class AVLNode<T> {

        // Parámetros y constructor
        protected int key;
        protected int height;
        protected ArrayList<T> dataList;
        protected AVLNode<T> left, right;

        public AVLNode (int key, T data){
            this.key = key;
            this.height = 1;
            this.dataList = new ArrayList<T>();
            this.dataList.add(data);
            this.left = null;
            this.right = null;
        }
        public boolean isLeaf(){
            return this.left == null && this.right == null;
        }

        // Actualización de altura
        public void updateHeight(){
            int leftHeight = (this.left == null) ? 0 : this.left.height;
            int rightHeight = (this.right == null) ? 0 : this.right.height;
            this.height = 1 + Math.max(leftHeight, rightHeight);
        }

        // Factor de balanceo
        public int getBalanceFactor(){
            int leftHeight = (this.left == null) ? 0 : this.left.height;
            int rightHeight = (this.right == null) ? 0 : this.right.height;
            return leftHeight - rightHeight;
        }
    }
// ---------------------------------- (Inicio de métodos de legado)

    // Parámetro inicial y constructor
    protected AVLNode<T> root;
    public AVL(){
		this.root = null;
	}

    // Raíz o vacío
	public boolean isRoot (AVLNode<T> n){
		return n == this.root;
	}
	public boolean isEmpty(){
		return this.root == null;
	}

    // Rotaciones
    private AVLNode<T> rotateL(AVLNode<T> node){
        // Word for word lo que tenía en BST, más el casting
        if (node == null || node.right == null) return node;
        AVLNode<T> new_root = node.right;
        node.right = new_root.left;
        new_root.left = node;

        // Consideraciones para AVL
        node.updateHeight();
        new_root.updateHeight();
        return new_root;
    }

    private AVLNode<T> rotateR(AVLNode<T> node){
        // Word for word lo que tenía en BST, más el casting
        if (node == null || node.left == null) return node;
        AVLNode<T> new_root = node.left;
        node.left = new_root.right;
        new_root.right = node;

        // Consideraciones para AVL
        node.updateHeight();
        new_root.updateHeight();
        return new_root;
    }

    // Búsquedas
    // Devuelve la lista asociada al nodo
    public ArrayList<T> search (int key){
        return searchRec(root, key);
    }
        protected ArrayList<T> searchRec(AVLNode<T> node, int key){
            if (node == null) { // No hay nada, devuelve una lista vacía
                return new ArrayList<T>();
            } 
            if (key == node.key) { // Caso base
                return node.dataList;
            } 
        return key < node.key ? searchRec(node.left, key) : searchRec(node.right, key);
        }

    // Devuelve el nodo completo
    protected AVLNode<T> findNode(int key){
        return findNodeRec(root, key);
    }
        protected AVLNode<T> findNodeRec(AVLNode<T> node, int key){
            // Casos base
            if (node == null) return null;
            if (node.key == key) return node;
            // Búsqueda recursiva
            return key < node.key ? findNodeRec(node.left, key) : findNodeRec(node.right, key);
        }

    // Eliminaciones
    // Mínimo de un árbol
        protected AVLNode<T> findMin(AVLNode<T> node){
            while (node.left != null) node = node.left;
            return node;
        }

    // Eliminación de un elemento contenido en la lista asociada
    public void deleteData(int key, T data){
        AVLNode<T> node = findNode(key); // Nueva referencia al nodo.
        if (node != null){
            node.dataList.remove(data);
            if (node.dataList.isEmpty()){
                delete(key);
            }
    } else{
            System.out.println("¡No existe!");
        }
    }
    
// ---------------------------------- (Fin de métodos de legado)

   
// Balanceo
    private AVLNode<T> balance(AVLNode<T> node){
        int bf = node.getBalanceFactor();
        // Casos de izquierda cargada: LL y LR
        if (bf > 1){
            if(node.left != null && (node.left.getBalanceFactor() >= 0)){
                return rotateR(node); // LL
            } else{
                node.left = rotateL(node.left);
                return rotateR(node);
            }
        }
        // Casos de derecha cargada: RR y RL
        else if (bf < -1){
            if (node.right != null &&  (node.right.getBalanceFactor() <= 0)){
                return rotateL(node); // RR
            } else{
                node.right = rotateR(node.right);
                return rotateL(node); // RL 
            }
        }
        // Árbol ya balanceado
        else {
            return node;
        }
    }

// Inserción
	public void insert (int key, T data){
		root = insertRec(root, key, data);
	}
        protected AVLNode<T> insertRec(AVLNode<T> node, int key, T data) {
            if (node == null) return new AVLNode<>(key, data);
        
            AVLNode<T> avlNode = node;
            if (key < avlNode.key) {
                avlNode.left = insertRec(avlNode.left, key, data);
            } else if (key > avlNode.key) {
                avlNode.right = insertRec(avlNode.right, key, data);
            } else {
                avlNode.dataList.add(data);
            }
        
            avlNode.updateHeight();
            return balance(avlNode);
        }

// Eliminación
	public void delete (int key){
		root = deleteRec( root, key);
	}
		private AVLNode<T> deleteRec(AVLNode<T> node, int key){
			if (node == null) return null;
            AVLNode<T> anode = node;
			if (key < anode.key ){
				anode.left = deleteRec(anode.left, key);
			} else if (key > anode.key){
				anode.right = deleteRec(anode.right, key);
			} else //YA LLEGAMOS YEEHAA
			// Caso 1: O es hoja o tiene únicamente un hijo
			if (anode.left == null) return anode.right;
			if (anode.right == null) return anode.left;
			// Caso 2: Tiene dos hijos
			// Creación de refencia al mímino del subárbol derecho
			AVLNode<T> sucesor = findMin(anode.right);
			// Se copian los datos del mínimo del derecho al actual
			anode.key = sucesor.key;
			anode.dataList = sucesor.dataList;
			// Se elimina del subábol derecho el nodo mínimo
			anode.right = deleteRec(anode.right, sucesor.key);
			// Actualización de alturas y balanceo
			anode.updateHeight();
			return balance(anode);
		}
}
