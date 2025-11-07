package com.example.model.strategy.emparejamiento;

import com.example.model.entity.Deporte;
import com.example.model.entity.Jugador;
import com.example.model.entity.Partido;
import com.example.notification.observer.INotificationObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * GestorEmparejamiento se encarga de almacenar los partidos disponibles y
 * aplicar una estrategia de emparejamiento para un jugador dado.  Este
 * componente utiliza el patrón Singleton para exponer una única instancia
 * global accesible desde cualquier parte de la aplicación.  Mediante el
 * patrón Strategy permite variar el algoritmo de búsqueda de partidos sin
 * modificar el código cliente.  Finalmente, se comporta como un pequeño
 * repositorio en memoria donde se registran todos los encuentros creados
 * mediante {@link #crearPartido(Jugador, Deporte, int)} o
 * {@link #agregarPartido(Partido)}.
 */
public class GestorEmparejamiento {
    /**
     * Instancia única del gestor (patrón Singleton).  Se inicializa de forma
     * perezosa cuando se invoca {@link #getInstance()} por primera vez.
     */
    private static final GestorEmparejamiento INSTANCE = new GestorEmparejamiento();

    /**
     * Lista de partidos actualmente disponibles en el sistema.  Este gestor no
     * persiste información y funciona únicamente como almacenamiento en
     * memoria.  En una aplicación real podría delegarse a un repositorio o
     * servicio externo.
     */
    private final List<Partido> partidos = new ArrayList<>();

    /**
     * Estrategia de emparejamiento que se utilizará para filtrar los
     * encuentros disponibles cuando se invoque {@link #buscarPartidosPara(Jugador)}.
     * Si no se establece una estrategia, el gestor devolverá la lista
     * completa de partidos.
     */
    private IEmparejamientoEstrategia estrategia;

    /**
     * Observer de notificaciones que se suscribirá automáticamente
     * a todos los partidos creados a través de este gestor.
     */
    private INotificationObserver notificationObserver;

    /**
     * Constructor privado para evitar instanciación externa.  Utilizar
     * {@link #getInstance()} para obtener el gestor.
     */
    private GestorEmparejamiento() {
        // ocultar constructor
    }

    /**
     * Devuelve la instancia única de GestorEmparejamiento.
     *
     * @return instancia singleton
     */
    public static GestorEmparejamiento getInstance() {
        return INSTANCE;
    }

    /**
     * Establece la estrategia de emparejamiento actual.  Pasar {@code null}
     * desactivará cualquier filtrado, devolviendo la lista completa de
     * partidos.
     *
     * @param estrategia la estrategia a utilizar
     */
    public void setEstrategia(IEmparejamientoEstrategia estrategia) {
        this.estrategia = estrategia;
    }

    /**
     * Establece el observer de notificaciones que se suscribirá automáticamente
     * a todos los partidos creados.
     *
     * @param observer el observer de notificaciones
     */
    public void setNotificationObserver(INotificationObserver observer) {
        this.notificationObserver = observer;
    }

    /**
     * Agrega un partido existente al listado gestionado.  Si el argumento
     * es {@code null} no se realiza ninguna acción. Si hay un notification
     * observer configurado, se suscribe automáticamente al partido.
     *
     * @param partido partido a almacenar
     */
    public void agregarPartido(Partido partido) {
        if (partido != null) {
            this.partidos.add(partido);
            if (notificationObserver != null) {
                partido.addObserver(notificationObserver);
            }
        }
    }

    /**
     * Crea un nuevo partido a partir de los parámetros proporcionados,
     * lo almacena internamente y lo devuelve al invocante.  El partido se
     * inicializa en el estado "Necesitamos jugadores" según la lógica
     * definida en la clase {@link Partido}. Si hay un notification observer
     * configurado, se suscribe automáticamente al partido.
     * 
     * NOTA: El evento PARTIDO_CREADO debe ser disparado manualmente llamando
     * a partido.publicarCreacion() después de configurar todos los datos.
     *
     * @param organizador jugador que organiza el encuentro
     * @param deporte     deporte del partido
     * @param jugadoresRequeridos cantidad de jugadores que se necesitan para
     *                           considerar que el partido está armado
     * @return el partido recién creado
     */
    public Partido crearPartido(Jugador organizador, Deporte deporte, int jugadoresRequeridos) {
        Partido partido = new Partido(organizador, deporte, jugadoresRequeridos);
        agregarPartido(partido);
        // No disparar el evento aquí - se debe llamar manualmente después
        // de mostrar el mensaje al usuario para mejor UX
        return partido;
    }

    /**
     * Devuelve una copia de la lista de partidos almacenados.  Se expone
     * solamente un clon para evitar que el cliente modifique el estado
     * interno del gestor.
     *
     * @return lista de partidos
     */
    public List<Partido> getPartidos() {
        return new ArrayList<>(partidos);
    }

    /**
     * Aplica la estrategia configurada para obtener aquellos partidos que
     * resulten adecuados al jugador dado.  Si no se ha configurado una
     * estrategia previamente, se devolverá la lista completa de partidos.
     *
     * @param jugador jugador para el cual se buscan encuentros
     * @return lista de partidos recomendados
     */
    public List<Partido> buscarPartidosPara(Jugador jugador) {
        List<Partido> copia = new ArrayList<>(partidos);
        if (estrategia == null || jugador == null) {
            return copia;
        }
        return estrategia.emparejar(copia, jugador);
    }
}
