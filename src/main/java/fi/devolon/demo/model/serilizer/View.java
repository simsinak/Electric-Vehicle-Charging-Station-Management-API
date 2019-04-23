package fi.devolon.demo.model.serilizer;

public interface View {
    interface Station{}
    interface StationWithDistance extends Station{}
    interface Company{
        interface ChildStations{}
    }
}
