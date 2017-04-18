package moises.com.appcoer.api;

public interface API {
    //http://app.coer.org.ar/api/users/login
    String COER = "http://app.coer.org.ar";

    String API = "/api";
    String USERS = API + "/users";
    String CHANGE_PASSWORD = USERS + "/changepassword";
    String RESET_PASSWORD = USERS + "/resetpassword";

    String LOGIN = USERS + "/login";
    String RESERVES_LIST = USERS + "/reservas";
    String BILLS = USERS + "/bills";
    String NEWS = API + "/noticias";
    String COURSES = API + "/cursos";
    String LODGINGS = API + "/alojamientos";
    String ROOMS = API + "/habitaciones";
    String ROOM_LIST = LODGINGS + "/{id}/habitaciones";
    String RESERVES = ROOMS + "/{id}/reservas";
    String ROOM_DATES_BUSY = ROOMS + "/{id}/fechas";

    String SITES = API + "/sites";
    String ENROLLMENT_DATE = SITES + "/matriculation";
    String METHOD_PAYMENTS = SITES + "/mediosdepago";
    String PROCESSES = SITES + "/tramites";
    String PROCESS_DETAIL = PROCESSES + "/{id}";
}
