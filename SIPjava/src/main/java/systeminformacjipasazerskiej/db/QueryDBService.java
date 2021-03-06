package systeminformacjipasazerskiej.db;

import systeminformacjipasazerskiej.converter.BoolConverter;
import systeminformacjipasazerskiej.converter.DayConverter;
import systeminformacjipasazerskiej.model.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class QueryDBService {
    private Connection connection;

    public QueryDBService(Connection connection) {
        this.connection = connection;
    }

    public Wagon getWagonById(int idWagonu) {
        Wagon wagon = new Wagon();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM wagony WHERE id_wagonu = " + idWagonu + ";"
            );

            if(resultSet.next()) {
                wagon.setIdWagonu(resultSet.getInt("id_wagonu"));
                wagon.setModel(resultSet.getString("model_wagonu"));
                wagon.setTyp(resultSet.getString("typ_wagonu"));
                wagon.setMiejscaI(resultSet.getInt("liczba_miejsc_I"));
                wagon.setMiejscaII(resultSet.getInt("liczba_miejsc_II"));
                wagon.setRowery(resultSet.getInt("liczba_rowerow"));
                wagon.setCzyPrzedzialowy(BoolConverter.convertBool(resultSet.getString("czy_przedzialowy")));
                wagon.setCzyKlimatyzacja(BoolConverter.convertBool(resultSet.getString("czy_klimatyzacja")));
                wagon.setCzyWifi(BoolConverter.convertBool(resultSet.getString("czy_wifi")));
                wagon.setCzyNiepelnosprawni(BoolConverter.convertBool(resultSet.getString("czy_niepelnosprawni")));
                wagon.setDlugosc(resultSet.getDouble("dlugosc_wagonu"));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wagon;
    }

    public Sklad getSkladById(int idSkladu) {
        Sklad sklad = new Sklad();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM sklady WHERE id_skladu = " + idSkladu + ";"
            );

            if(resultSet.next()) {
                sklad.setIdSkladu(resultSet.getInt("id_skladu"));
                sklad.setCzyPrzesylki(BoolConverter.convertBool(resultSet.getString("czy_przesylki")));
            }

            resultSet = statement.executeQuery(
                "SELECT * FROM sklady_wagony WHERE id_skladu = " + idSkladu + ";"
            );

            ArrayList<Wagon> listaWagonow = new ArrayList<>();
            ArrayList<Integer> idWagonow = new ArrayList<>();
            ArrayList<Integer> liczbaWagonow = new ArrayList<>();
            while(resultSet.next()) {
                int idWagonu = resultSet.getInt("id_wagonu");
                idWagonow.add(idWagonu);
                listaWagonow.add(getWagonById(idWagonu));
                liczbaWagonow.add(resultSet.getInt("liczba_wagonow"));
            }

            sklad.setListaWagonow(listaWagonow);
            sklad.setIdWagonow(idWagonow);
            sklad.setLiczbaWagonow(liczbaWagonow);

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sklad;
    }

    public Stacja getStacjaById(int idStacji) {
        Stacja stacja = new Stacja();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM stacje WHERE id_stacji = " + idStacji + ";"
            );

            if(resultSet.next()) {
                stacja.setIdStacji(resultSet.getInt("id_stacji"));
                stacja.setNazwaStacji(resultSet.getString("nazwa_stacji"));
                stacja.setLiczbaTorow(resultSet.getInt("liczba_torow"));
                stacja.setLiczbaPeronow(resultSet.getInt("liczba_peronow"));
                stacja.setDlugoscPeronu(resultSet.getDouble("dlugosc_peronu"));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stacja;
    }

    public Pociag getPociagById(int idPociagu) {
        Pociag pociag = new Pociag();

        try {
            System.out.println("Searching for trains");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM pociagi WHERE id_pociagu = " + idPociagu + ";"
            );

            if(resultSet.next()) {
                pociag.setIdPociagu(resultSet.getInt("id_pociagu"));
                pociag.setIdTrasy(resultSet.getInt("id_trasy"));
                pociag.setNazwaPociagu(resultSet.getString("nazwa_pociagu"));
                pociag.setTypPociagu(resultSet.getString("typ_pociagu"));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pociag;
    }

    public Postoj getPostojByIds(int idKursu, int idStacji) {
        Postoj postoj = new Postoj();

        try {
            System.out.println("Searching for postoje");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM postoje WHERE id_kursu = " + idKursu + "AND id_stacji = " + idStacji + ";"
            );

            if(resultSet.next()) {
                postoj.setIdKursu(resultSet.getInt("id_kursu"));
                postoj.setStacja(getStacjaById(resultSet.getInt("id_stacji")));
                postoj.setNastepnySklad(resultSet.getInt("nastepny_sklad"));
                postoj.setOdjazd(resultSet.getString("odjazd"));
                postoj.setPrzyjazd(resultSet.getString("przyjazd"));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return postoj;
    }


    public ArrayList<Stacja> getAllStations() {
        ArrayList<Stacja> stacje = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM stacje ORDER BY nazwa_stacji;"
            );

            while(resultSet.next()) {
                Stacja stacja = new Stacja();
                stacja.setIdStacji(resultSet.getInt("id_stacji"));
                stacja.setNazwaStacji(resultSet.getString("nazwa_stacji"));
                stacja.setLiczbaTorow(resultSet.getInt("liczba_torow"));
                stacja.setLiczbaPeronow(resultSet.getInt("liczba_peronow"));
                stacja.setDlugoscPeronu(resultSet.getDouble("dlugosc_peronu"));

                stacje.add(stacja);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stacje;
    }

    public ArrayList<Pociag> getAllPociagi() {
        ArrayList<Pociag> pociagi = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM pociagi ORDER BY nazwa_pociagu;"
            );

            while(resultSet.next()) {
                Pociag pociag = new Pociag();
                pociag.setIdPociagu(resultSet.getInt("id_pociagu"));
                pociag.setIdTrasy(resultSet.getInt("id_trasy"));
                pociag.setNazwaPociagu(resultSet.getString("nazwa_pociagu"));
                pociag.setTypPociagu(resultSet.getString("typ_pociagu"));
                pociagi.add(pociag);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pociagi;
    }

    public ArrayList<Wagon> getAllWagony() {
        ArrayList<Wagon> wagony = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM wagony ORDER BY model_wagonu;"
            );

            while(resultSet.next()) {
                Wagon wagon = new Wagon();
                wagon.setIdWagonu(resultSet.getInt("id_wagonu"));
                wagon.setModel(resultSet.getString("model_wagonu"));
                wagon.setTyp(resultSet.getString("typ_wagonu"));
                wagon.setMiejscaI(resultSet.getInt("liczba_miejsc_I"));
                wagon.setMiejscaII(resultSet.getInt("liczba_miejsc_II"));
                wagon.setRowery(resultSet.getInt("liczba_rowerow"));
                wagon.setCzyPrzedzialowy(resultSet.getBoolean("czy_przedzialowy"));
                wagon.setCzyKlimatyzacja(resultSet.getBoolean("czy_przedzialowy"));
                wagon.setCzyWifi(resultSet.getBoolean("czy_wifi"));
                wagon.setCzyNiepelnosprawni(resultSet.getBoolean("czy_niepelnosprawni"));
                wagon.setDlugosc(resultSet.getDouble("dlugosc_wagonu"));
                wagony.add(wagon);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wagony;
    }

    public ArrayList<Wagon> getWagonsByModel(String model) throws NoSuchModelException {
        ArrayList<Wagon> wagony = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM wagony WHERE model_wagonu = '" + model + "';");
            int cnt = 0;

            while(resultSet.next()) {
                cnt ++;
                Wagon wagon = new Wagon();
                wagon.setIdWagonu(resultSet.getInt("id_wagonu"));
                wagon.setModel(resultSet.getString("model_wagonu"));
                wagon.setTyp(resultSet.getString("typ_wagonu"));
                wagon.setMiejscaI(resultSet.getInt("liczba_miejsc_I"));
                wagon.setMiejscaII(resultSet.getInt("liczba_miejsc_II"));
                wagon.setRowery(resultSet.getInt("liczba_rowerow"));
                wagon.setCzyPrzedzialowy(resultSet.getBoolean("czy_przedzialowy"));
                wagon.setCzyKlimatyzacja(resultSet.getBoolean("czy_przedzialowy"));
                wagon.setCzyWifi(resultSet.getBoolean("czy_wifi"));
                wagon.setCzyNiepelnosprawni(resultSet.getBoolean("czy_niepelnosprawni"));
                wagon.setDlugosc(resultSet.getDouble("dlugosc_wagonu"));
                wagony.add(wagon);
            }
            if(cnt == 0) throw new NoSuchModelException();

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wagony;
    }

    public Sklad calculateSkladKursu(Kurs kurs) {
        ArrayList<Integer> idWagonow =
                getSkladById(kurs.getListaPostojow().get(0).getNastepnySklad()).getIdWagonow();

        for(int i = 1; i < kurs.getListaPostojow().size() - 1; i++) {
            ArrayList<Integer> nextSklad =
                    getSkladById(kurs.getListaPostojow().get(i).getNastepnySklad()).getIdWagonow();
            idWagonow.removeIf(w -> !nextSklad.contains(w));
        }

        ArrayList<Integer> liczbaWagonow = new ArrayList<>();
        for(int i = 0; i < idWagonow.size(); i++)
            liczbaWagonow.add(1000000000);

        boolean czyPrzesylki = true;
        for(int i = 0; i < kurs.getListaPostojow().size() - 1; i++) {
            Sklad nextSklad = getSkladById(kurs.getListaPostojow().get(i).getNastepnySklad());

            ArrayList<Integer> nextIdWagonow = nextSklad.getIdWagonow();
            ArrayList<Integer> nextLiczbaWagonow = nextSklad.getLiczbaWagonow();

            if(!nextSklad.isCzyPrzesylki())
                czyPrzesylki = false;

            for(int j = 0; j < nextIdWagonow.size(); j++) {
                int ind = idWagonow.indexOf(nextIdWagonow.get(j));
                if(ind == -1)
                    continue;

                if(liczbaWagonow.get(ind) > nextLiczbaWagonow.get(j))
                    liczbaWagonow.set(ind, nextLiczbaWagonow.get(j));
            }
        }

        ArrayList<Wagon> listaWagonow = new ArrayList<>();
        for(Integer i : idWagonow)
            listaWagonow.add(getWagonById(i));

        Sklad sklad = new Sklad();
        sklad.setIdSkladu(-1);
        sklad.setCzyPrzesylki(czyPrzesylki);
        sklad.setListaWagonow(listaWagonow);
        sklad.setIdWagonow(idWagonow);
        sklad.setLiczbaWagonow(liczbaWagonow);

        System.out.println(idWagonow + "\n" + liczbaWagonow);
        return sklad;
    }

    public ArrayList<Kurs> getConnections(String fromStation, String toStation,
                                          String day, String postojType, String time,
                                          boolean isPospieszny, boolean isEkspres, boolean isPendolino,
                                          boolean isRower, boolean isNiepelnosprawni)
            throws NoSuchStationException, NoMatchingKursyException {
        ArrayList<Kurs> kursy = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT id_stacji FROM stacje WHERE nazwa_stacji = '" + fromStation + "';"
            );
            int fromStationId = resultSet.next() ? resultSet.getInt("id_stacji") : -1;

            resultSet = statement.executeQuery(
                "SELECT id_stacji FROM stacje WHERE nazwa_stacji = '" + toStation + "';"
            );
            int toStationId = resultSet.next() ? resultSet.getInt("id_stacji") : -1;

            if(fromStationId == -1 || toStationId == -1)
                throw new NoSuchStationException();

            System.out.println("id_stacji ready " + fromStationId + " " + toStationId);
            System.out.println(day + " " + DayConverter.convertDay(day));

            if(postojType.equals("Przyjazd")) {
                resultSet = statement.executeQuery(
                    "SELECT ro.* " +
                    "FROM rozklady ro " +
                        "INNER JOIN postoje pos ON ro.id_kursu = pos.id_kursu " +
                    "WHERE getDay(ro.id_kursu, " + toStationId + ", '" + postojType + "') IS NOT DISTINCT FROM " +
                        DayConverter.convertDay(day) + " AND " +
                        "pos.id_stacji = " + toStationId + " AND pos.przyjazd <= '" + time + "'::time AND " +
                        "ro.id_pociagu IN " +
                            "(SELECT po.id_pociagu " +
                            "FROM pociagi po " +
                            "WHERE po.id_trasy IN " +
                                "(SELECT idTrasy " +
                                "FROM getIdTrasyFromTo(" + fromStationId + ", " + toStationId + "))) " +
                    "ORDER BY pos.przyjazd;"
                );
            } else {
                resultSet = statement.executeQuery(
                    "SELECT ro.* " +
                    "FROM rozklady ro " +
                        "INNER JOIN postoje pos ON ro.id_kursu = pos.id_kursu " +
                    "WHERE getDay(ro.id_kursu, " + fromStationId + ", '" + postojType + "') IS NOT DISTINCT FROM " +
                        DayConverter.convertDay(day) + " AND " +
                        "pos.id_stacji = " + fromStationId + " AND pos.odjazd >= '" + time + "'::time AND " +
                        "ro.id_pociagu IN " +
                            "(SELECT po.id_pociagu " +
                            "FROM pociagi po " +
                            "WHERE po.id_trasy IN " +
                                "(SELECT idTrasy " +
                                "FROM getIdTrasyFromTo(" + fromStationId + ", " + toStationId + "))) " +
                    "ORDER BY pos.odjazd;"
                );
            }

            while(resultSet.next()) {
                Kurs kurs = new Kurs();
                kurs.setIdKursu(resultSet.getInt("id_kursu"));
                kurs.setPociag(getPociagById(resultSet.getInt("id_pociagu")));

                String typ = kurs.getPociag().getTypPociagu();
                if ((typ.equals("pospieszny") && isPospieszny) ||
                    (typ.equals("ekspres") && isEkspres) ||
                    (typ.equals("pendolino") && isPendolino)) {
                    kursy.add(kurs);
                }
            }

            System.out.println("kursy ready, size " + kursy.size());

            for(Kurs kurs : kursy) {
                resultSet = statement.executeQuery(
                    "SELECT pos.* " +
                    "FROM " +
                        "(SELECT idStacji " +
                        "FROM getStationsBetween(" + kurs.getIdKursu() + ", " + fromStationId + ", " + toStationId + ")) st " +
                        "INNER JOIN postoje pos ON st.idStacji = pos.id_stacji " +
                    "WHERE pos.id_kursu = " + kurs.getIdKursu() + ";"
                );

                ArrayList<Postoj> listaPostojow = new ArrayList<>();

                while(resultSet.next()) {
                    Postoj postoj = new Postoj();
                    postoj.setIdKursu(resultSet.getInt("id_kursu"));
                    postoj.setStacja(getStacjaById(resultSet.getInt("id_stacji")));
                    postoj.setPrzyjazd(resultSet.getString("przyjazd"));
                    postoj.setOdjazd(resultSet.getString("odjazd"));
                    postoj.setNastepnySklad(resultSet.getInt("nastepny_sklad"));

                    listaPostojow.add(postoj);
                }

                kurs.setListaPostojow(listaPostojow);
                kurs.calculateCzasPrzejazdu();
                kurs.setSkladKursu(calculateSkladKursu(kurs));

                System.out.println("added postoje for kurs " + kurs.getIdKursu());
            }

            resultSet.close();
            statement.close();

            if(isRower)
                kursy.removeIf(k -> k.getSkladKursu().getLiczbaMiejscDlaRowerow() == 0);
            if(isNiepelnosprawni)
                kursy.removeIf(k -> !k.getSkladKursu().checkIfNiepelnosprawni());

            if(kursy.isEmpty())
                throw new NoMatchingKursyException();

            System.out.println("final kursy size " + kursy.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return kursy;
    }

    public ArrayList<Destination> getDestinations(String fromStation) throws NoSuchStationException {
        ArrayList<Destination> destinations = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT id_stacji FROM stacje WHERE nazwa_stacji = '" + fromStation + "';"
            );
            int fromStationId = resultSet.next() ? resultSet.getInt("id_stacji") : -1;

            if(fromStationId == -1)
                throw new NoSuchStationException();

            resultSet = statement.executeQuery(
                "SELECT id_trasy " +
                "FROM trasy_odcinki trod " +
                    "INNER JOIN odcinki od ON trod.id_odcinka = od.id_odcinka " +
                "WHERE od.stacja_poczatkowa = " + fromStationId + ";"
            );

            ArrayList<Integer> trasy = new ArrayList<>();
            while(resultSet.next())
                trasy.add(resultSet.getInt("id_trasy"));

            for(Integer idTrasy : trasy) {
                System.out.println("found id trasy " + idTrasy);

                int lastStation = getLastStationFromTrasa(idTrasy);
                String dest = getStacjaById(lastStation).getNazwaStacji();

                Destination destination = null;
                for(Destination d : destinations) {
                    if(d.getMainDestination().equals(dest)) {
                        destination = d;
                        break;
                    }
                }

                if(destination == null) {
                    destination = new Destination();
                    destinations.add(destination);
                }

                destination.setSource(fromStation);
                destination.setMainDestination(dest);

                ArrayList<String> stacjePosrednie = destination.getStacjePosrednie();

                resultSet = statement.executeQuery(
                    "SELECT * FROM getStationsBetweenOnTrasa(" + idTrasy + ", " + fromStationId + ", " + lastStation + ");"
                );
                while(resultSet.next())
                    stacjePosrednie.add(getStacjaById(resultSet.getInt(1)).getNazwaStacji());
            }

            for(Destination d : destinations) {
                ArrayList<String> stacjePosrednie = new ArrayList<>(new HashSet<>(d.getStacjePosrednie()));
                Collections.sort(stacjePosrednie);
                d.setStacjePosrednie(stacjePosrednie);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return destinations;
    }


    public int getFirstStationFromTrasa(int id_trasy) {
        int id_stacji = 0;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM getFirstStation("+id_trasy+");"
            );
            if(resultSet.next())
                id_stacji = resultSet.getInt(1);

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id_stacji;
    }

    public int getLastStationFromTrasa(int id_trasy) {
        int id_stacji = 0;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM getLastStation("+id_trasy+");"
            );
            if(resultSet.next())
                id_stacji = resultSet.getInt(1);

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id_stacji;
    }

    public ArrayList<Integer> getTrasaIdFromTo (String fromStation, String toStation)
            throws NoSuchStationException, NoMatchingTrasyException {
        ArrayList<Integer> id_trasy = new ArrayList<>();

        try {
            int fromStationId, toStationId;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT id_stacji FROM stacje WHERE nazwa_stacji = '" + fromStation + "';"
            );
            if(resultSet.next())
                fromStationId = resultSet.getInt("id_stacji");
            else {
                statement.close();
                resultSet.close();
                throw new NoSuchStationException();
            }

            resultSet = statement.executeQuery(
                "SELECT id_stacji FROM stacje WHERE nazwa_stacji = '" + toStation + "';"
            );
            if(resultSet.next())
                toStationId = resultSet.getInt("id_stacji");
            else {
                statement.close();
                resultSet.close();
                throw new NoSuchStationException();
            }

            resultSet = statement.executeQuery(
                "SELECT idTrasy " +
                "FROM getIdTrasyFromTo(" + fromStationId + ", " + toStationId + ") " +
                "ORDER BY idTrasy;"
            );
            while (resultSet.next())
                id_trasy.add(resultSet.getInt("idTrasy"));

            resultSet.close();
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        if(id_trasy.isEmpty())
            throw new NoMatchingTrasyException();

        return id_trasy;
    }

    public ArrayList<Stacja> getAllStacjeOnTrasa(int id_trasy, int fromStationId, int toStationId) {
        ArrayList<Stacja> stacje = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();

            stacje.add(getStacjaById(fromStationId));

            System.out.println("id trasy: " + id_trasy);
            System.out.println("from: " + fromStationId);
            System.out.println("to: " + toStationId);

            ResultSet resultSet = statement.executeQuery(
                "SELECT idStacji FROM getStationsBetweenOnTrasa(" + id_trasy + ", " + fromStationId + ", " + toStationId + ");"
            );

            while (resultSet.next())
                stacje.add(getStacjaById(resultSet.getInt("idStacji")));

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stacje;
    }

    public Kurs getWholeKursFromPart(Kurs kursPart) {
        Kurs wholeKurs = new Kurs();
        wholeKurs.setIdKursu(kursPart.getIdKursu());
        wholeKurs.setPociag(kursPart.getPociag());
        Pociag pociag = wholeKurs.getPociag();
        int idTrasy = pociag.getIdTrasy();
        int fromStationId = getFirstStationFromTrasa(idTrasy);
        int toStationId = getLastStationFromTrasa(idTrasy);

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT pos.* " +
                "FROM " +
                    "(SELECT idStacji " +
                    "FROM getStationsBetween(" + wholeKurs.getIdKursu() + ", " + fromStationId + ", " + toStationId + ")) st " +
                    "INNER JOIN postoje pos ON st.idStacji = pos.id_stacji " +
                "WHERE pos.id_kursu = " + wholeKurs.getIdKursu() + ";"
            );

            ArrayList<Postoj> listaPostojow = new ArrayList<>();

            while(resultSet.next()) {
                Postoj postoj = new Postoj();
                postoj.setIdKursu(resultSet.getInt("id_kursu"));
                postoj.setStacja(getStacjaById(resultSet.getInt("id_stacji")));
                postoj.setPrzyjazd(resultSet.getString("przyjazd"));
                postoj.setOdjazd(resultSet.getString("odjazd"));
                postoj.setNastepnySklad(resultSet.getInt("nastepny_sklad"));

                listaPostojow.add(postoj);
            }

            wholeKurs.setListaPostojow(listaPostojow);
            wholeKurs.calculateCzasPrzejazdu();
            wholeKurs.setSkladKursu(calculateSkladKursu(wholeKurs));

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wholeKurs;
    }

    public ArrayList<Sklad> getSkladByNumber(int number, String model) throws NoSuchSkladException {
        ArrayList<Sklad> sklady = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT idskladu FROM getSkladByNumber( " + number +", '"+ model + "');"
            );

            if(resultSet.next()) {
                sklady.add(getSkladById(resultSet.getInt("idskladu")));
            } else throw new NoSuchSkladException();

            while (resultSet.next()) sklady.add(getSkladById(resultSet.getInt("idskladu")));
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sklady;
    }

    public ArrayList<Sklad> getSkladByNumber(int number) throws NoSuchSkladException {
        ArrayList<Sklad> sklady = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT idskladu FROM getSkladByNumber( " + number +" );"
            );

            if(resultSet.next()) {
                sklady.add(getSkladById(resultSet.getInt("idskladu")));
            } else throw new NoSuchSkladException();

            while (resultSet.next()) sklady.add(getSkladById(resultSet.getInt("idskladu")));
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sklady;
    }

    public ArrayList<Sklad> getSkladByNumber(String model) throws NoSuchSkladException {
        ArrayList<Sklad> sklady = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT idskladu FROM getSkladByNumber( '" + model + "');"
            );

            if(resultSet.next()) {
                sklady.add(getSkladById(resultSet.getInt("idskladu")));
            } else throw new NoSuchSkladException();

            while (resultSet.next()) sklady.add(getSkladById(resultSet.getInt("idskladu")));
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sklady;
    }


    public static class NoSuchStationException extends Exception {}

    public static class NoSuchTrainException extends Exception {}

    public static class NoMatchingKursyException extends Exception {}

    public static class NoMatchingTrasyException extends Exception {}

    public static class NoSuchModelException extends Exception {}

    public static class NoSuchSkladException extends Exception {}

}

