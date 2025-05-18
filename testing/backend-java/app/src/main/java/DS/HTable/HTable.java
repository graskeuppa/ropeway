/*
 * e.g.bollás
 * Analogía: Buzones de correo en un edificio residencial
 *
 * Se puede imaginar a la tabla hash como un buzón de correo en un edificio de residencia.
 *
 * Cada caja de correo (bucket) y cada residente (par valor-llave) recibe correo.
 *
 * Los dueños del edificio emplean una regla (función hash) para determinar la caja
 * de correo que cada residente debería usar dentro del buzón. 
 * En caso de que múltiples residentes tengan asignada la misma caja de correo (colisiones), 
 * se forman listas (LinkedList) dentro de ellas.
 *
 * Por trozos:
 *
 */
package DS.HTable;

import java.util.LinkedList;

public class HTable<K, V> {
    ; // Las cartas que entran a las cajas de correo llevan un nombre (key) y
      // contenido (value)
    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Cada caja de correo dentro del buzón:
    // Cada caja de correo contiene un fichero de cartas
    private LinkedList<Entry<K, V>>[] buckets;
    // La capacidad actual del buzón completo
    private int capacity;
    // La cantidad de cartas en todo el buzón-
    private int size;
    // ... capacidad inicial del buzón
    private static final int INITIAL_CAPACITY = 16;
    // Límite proporcional inicial de sobrecarga cartas v. cajas de correo.
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;

    // Constructor: inicializa el buzón
    public HTable() {
        // La cantidad inicial de cajas en el buzón es de 16.
        this.capacity = INITIAL_CAPACITY;
        // Crea 16 cajas de correo separadas, cada una con un fichero inicialmente vacío
        @SuppressWarnings("unchecked")
        LinkedList<Entry<K, V>>[] temp = (LinkedList[]) new LinkedList[capacity];
        this.buckets = temp; // Esto es porque Java no te deja crear arreglos genéricos en runtime.
        // Cantidad de cartas total en el buzón
        this.size = 0;
    }

    // Regla del edificio para determinar la caja de correos a usar en base al
    // nombre
    private int getIndex(K key) {
        return Math.abs(key.hashCode()) % capacity; // Debería reformular esto.
    }

    // PUT: La entrega de una nueva carta (par llave-valor) en la caja aproiada
    public void put(K key, V value) {
        // Obtén la caja correcta en base al nombre
        int index = getIndex(key);

        // Si aún no hay un fichero de cartas en la caja, inicia uno
        if (buckets[index] == null) {
            buckets[index] = new LinkedList<>();
        }

        // Para cada carta en el fichero de la caja actual, si hay dos dirigidas
        // a la misma persona, conserva la más nueva
        for (Entry<K, V> entry : buckets[index]) { // Para cada carta en el fichero:
            if (entry.key.equals(key)) { // si la persona de la carta vieja es la
                                         // misma que la de la nueva
                entry.value = value; // entonces pon el mensaje de la nueva en
                                     // donde la vieja.
                return; // Termina antes la ejecución. Se salta lo sig.
            }
        }

        buckets[index].add(new Entry<>(key, value)); // Si no hubo repetidos, entonces añade
                                                     // al fichero de la caja una nueva carta
                                                     // con los datos dados.
        size++; // Incrementa en uno el total de las cartas
                // en todo el buzón-

        if (size / capacity > LOAD_FACTOR_THRESHOLD) { // Si la cantidad de cartas comienza a
                                                       // superar a la capacidad y esto es mayor
                                                       // que el factor de llenado original,
                                                       // actualiza el tamaño del buzón completo.
            resize();
        }
    }

    // GET: Revisar en qué caja de correo está una carta en base al nombre del
    // residente, leerla
    public V get(K key) {
        // Obtén la caja correcta con el nombre
        int index = getIndex(key);
        // Referencia nueva al fichero correspondiente a la caja hallada por nombre
        LinkedList<Entry<K, V>> bucket = buckets[index];

        if (bucket != null) { // Si el fichero de la caja no está vacio
            for (Entry<K, V> entry : bucket) { // Para cada carta en el fichero
                if (entry.key.equals(key)) { // Si el nombre en la carta es igual al
                                             // del que la busca
                    return entry.value; // Revisa lo que tiene escrito, es suya, duh.
                }
            }
        }
        return null; // Entonces no se encontró ninguna carta a ese
                     // nombre
    }

    // REMOVE: Sacar el correo de la caja dado el nombre del residente. Lee la carta
    // y la saca
    public V remove(K key) {
        // Obtén la caja correcta con el nombre
        int index = getIndex(key);
        // Referencia nueva al fichero correspondiente a la caja hallada por nombre
        LinkedList<Entry<K, V>> bucket = buckets[index];

        if (bucket != null) { // Si el fichero no está vacío
            for (Entry<K, V> entry : bucket) { // Para cada carta en el fichero
                if (entry.key.equals(key)) { // Si el bombre en la carta es igual al
                                             // del que lo busca
                    // V value = entry.value; // OPCIONAL: Anota el mensaje en otro lado
                    bucket.remove(entry); // Quita la carta del fichero
                    size--; // Reduce en 1 la cantidad total de cartas
                    // return value; // OPCIONAL: Lee el mensaje.
                }
            }
        }
        return null; // No se encontró nignuna carta a ese nombre.
    }

    // RESIZE: Cuando se pasa de cierta cantidad de cartas, se duplican las cajas de
    // corre\ y se redistribuye el correo existente en base a una nueva regla.
    public void resize() {
        LinkedList<Entry<K, V>>[] oldBuckets = buckets; // Se guardan las viejas cajas
        capacity *= 2; // Se duplica la capacidad del buzón
        size = 0; // Se regresa por completo la cant. de cartas.
        @SuppressWarnings("unchecked")
        LinkedList<Entry<K, V>>[] temp = (LinkedList[]) new LinkedList[capacity];
        buckets = temp; // Las cajas actuales ahora son el doble de las
                        // anteriores, pero están vacías todas
        for (LinkedList<Entry<K, V>> oldBucket : oldBuckets) { // Para cada caja vieja del buzón
            if (oldBucket != null) { // Si el fichero no estaba vacío
                for (Entry<K, V> entry : oldBucket) { // Para cada carta en el fichero viejo
                    put(entry.key, entry.value); // Se meten en el del nuevo.
                }
            }
        }
    }

    // SIZE: Muestra la cantidad de cartas en el buzón
    public int size() {
        return size;
    }

    // CONTAINSKEY: Informa si la persona tiene correo (You´ve got mail!)
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    // PRINTTABLE: Da una representación gráfica del buzón
    public void printTable() {
        for (int i = 0; i < buckets.length; i++) {
            System.out.println("Caja de correo " + i + ":");
            if (buckets[i] != null) {
                for (Entry<K, V> entry : buckets[i]) {
                    System.out.println("╰──(" + entry.key + "): " + entry.value);
                }
            }
            System.out.println();
        }
    }
}
