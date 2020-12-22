<?php

namespace App\Http\Controllers;

use App\Models\PopisStavuRieseniaProblemu;
use App\Models\PriradeneVozidlo;
use App\Models\PriradenyZamestnanec;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use App\Models\Problem;
use App\Models\KategoriaProblemu;
use App\Models\StavProblemu;
use App\Models\Priorita;
use App\Models\Kraj;
use App\Models\KatastralneUzemie;
use App\Models\Obec;
use App\Models\Vozidlo;
use App\Models\Spravca;
use App\Models\TypStavuRieseniaProblemu;
use App\Models\StavRieseniaProblemu;
use Illuminate\Support\Facades\Auth;
use App\User;


class ProblemController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     *
     * nacitanie problemov
     * nacitanie posledneho zaznamu daneho problemu z tabulky stavy_riesenia_problemov,
     * kde sa nachadza aktualny stav riesenia pre dany problem
     * (ostatne stavy pred poslednym su historicke stavy riesenia)
     * get atribut typ_stavu_riesenia_problemu_id
     * vlozenie do pola stavy_riesenia
     */

    public function index(Request $request)
    {
        $rola = Auth::user()->rola_id;
        $user_id = Auth::user()->id;
        $typy_stavov = TypStavuRieseniaProblemu::all();



        if (($rola == 1) || ($rola == 2)) {

            $problems = Problem::where('pouzivatel_id', '=', $user_id)->paginate(10);
            $stavy_riesenia = array();
            foreach ($problems as $problem) {
                $typ = DB::table('stav_riesenia_problemu')
                    ->where('problem_id', '=', $problem->problem_id)
                    ->latest('stav_riesenia_problemu_id')->first();
                array_push($stavy_riesenia, $typ->typ_stavu_riesenia_problemu_id);
            }

            return view('problem.obcan.obcan_index')
                ->with('problems', $problems)
                ->with('stavy_riesenia', $stavy_riesenia)
                ->with('typy_stavov_riesenia', $typy_stavov);
        } else {


            $problems = Problem::paginate(10);
            $users = User::all();
            $vsetky_vozidla = Vozidlo::all();
            $stavy_riesenia = array();
            $priradeni_zamestnanci = array();
            $priradene_vozidla = array();

            $kategorie = KategoriaProblemu::all();
            $stavyProblemu = StavProblemu::all();
            $typySTavovRieseniaProblemu = TypStavuRieseniaProblemu::all();
            $priority = Priorita::all();
            $zamestnanciAll = User::where('rola_id', '>', '2')->get();


            foreach ($problems as $problem) {
                $typ = DB::table('stav_riesenia_problemu')
                    ->where('problem_id', '=', $problem->problem_id)
                    ->latest('stav_riesenia_problemu_id')->first();
                array_push($stavy_riesenia, $typ->typ_stavu_riesenia_problemu_id);

                $zamestnanci = DB::table('priradeny_zamestnanec')
                    ->where('problem_id', '=', $problem->problem_id)
                    ->latest('priradeny_zamestnanec_id')->first();
                if ($zamestnanci != null)
                    array_push($priradeni_zamestnanci, $zamestnanci->zamestnanec_id);
                else
                    array_push($priradeni_zamestnanci, 0);

                $vozidla = DB::table('priradene_vozidlo')
                    ->where('problem_id', '=', $problem->problem_id)
                    ->latest('priradene_vozidlo_id')->first();


                if ($vozidla != null)
                    array_push($priradene_vozidla, $vozidla->vozidlo_id);
                else
                    array_push($priradene_vozidla, 0);
            }


            if ($rola == 3)
                return view('problem.admin.admin_index')
                    ->with('problems', $problems)
                    ->with('stavy_riesenia', $stavy_riesenia)
                    ->with('typy_stavov_riesenia', $typy_stavov)
                    ->with('priradeni_zamestnanci', $priradeni_zamestnanci)
                    ->with('zamestnanci', $users)
                    ->with('priradene_vozidla', $priradene_vozidla)
                    ->with('vozidla', $vsetky_vozidla)
                    ->with('kategorie', $kategorie)
                    ->with('stavyProblemu', $stavyProblemu)
                    ->with('typyStavovRieseniaProblemu', $typySTavovRieseniaProblemu)
                    ->with('priority', $priority)
                    ->with('VsetciZamestnanci', $zamestnanciAll);
            if ($rola == 4)
                return view('problem.dispecer.dispecer_index')
                    ->with('problems', $problems)
                    ->with('stavy_riesenia', $stavy_riesenia)
                    ->with('typy_stavov_riesenia', $typy_stavov)
                    ->with('priradeni_zamestnanci', $priradeni_zamestnanci)
                    ->with('zamestnanci', $users)
                    ->with('priradene_vozidla', $priradene_vozidla)
                    ->with('vozidla', $vsetky_vozidla)
                    ->with('kategorie', $kategorie)
                    ->with('stavyProblemu', $stavyProblemu)
                    ->with('typyStavovRieseniaProblemu', $typySTavovRieseniaProblemu)
                    ->with('priority', $priority)
                    ->with('VsetciZamestnanci', $zamestnanciAll);

            if ($rola == 5)
                return view('problem.manazer.manazer_index')
                    ->with('problems', $problems)
                    ->with('stavy_riesenia', $stavy_riesenia)
                    ->with('typy_stavov_riesenia', $typy_stavov)
                    ->with('priradeni_zamestnanci', $priradeni_zamestnanci)
                    ->with('zamestnanci', $users)
                    ->with('priradene_vozidla', $priradene_vozidla)
                    ->with('vozidla', $vsetky_vozidla)
                    ->with('kategorie', $kategorie)
                    ->with('stavyProblemu', $stavyProblemu)
                    ->with('typyStavovRieseniaProblemu', $typySTavovRieseniaProblemu)
                    ->with('priority', $priority)
                    ->with('VsetciZamestnanci', $zamestnanciAll);
        }
    }


    public function filter(Request $request)
    {

        if($request->orderBy == "2" ){
            $final = Problem::orderBy('created_at', 'DESC')->get();
        }
        else{
            $final = Problem::orderBy('created_at', 'ASC')->get();
        }

        if($request->kategoria_problemu_id != null){
            foreach($final as $problem){
                $id = $problem->problem_id;
                if($problem->kategoria_problemu_id != $request->kategoria_problemu_id){

                    $key = $final->search(function ($item) use ($id) {
                        return $item->problem_id == $id;
                    });

                    $final->pull($key);
                }
            }
        }

        if($request->stav_problemu_id != null){
            foreach($final as $problem){
                $id = $problem->problem_id;
                if($problem->stav_problemu_id != $request->stav_problemu_id){

                    $key = $final->search(function ($item) use ($id) {
                        return $item->problem_id == $id;
                    });

                    $final->pull($key);
                }
            }
        }

        if($request->priorita_id != null){
            foreach($final as $problem){
                $id = $problem->problem_id;
                if($problem->priorita_id != $request->priorita_id){

                    $key = $final->search(function ($item) use ($id) {
                        return $item->problem_id == $id;
                    });

                    $final->pull($key);
                }
            }
        }


        if($request->pouzivatel_id != null){
            foreach($final as $problem){
                $id = $problem->problem_id;
                if($problem->pouzivatel_id != $request->pouzivatel_id){

                    $key = $final->search(function ($item) use ($id) {
                        return $item->problem_id == $id;
                    });

                    $final->pull($key);
                }
            }
        }

        if ($request->typ_stavu_riesenia_problemu_id != null) {
            foreach ($final as $problem) {

                $id = $problem->problem_id;

                $stav_riesenia = DB::table('stav_riesenia_problemu')
                    ->where('problem_id', '=', $problem->problem_id)
                    ->latest('stav_riesenia_problemu_id')->first();

                if ($request->typ_stavu_riesenia_problemu_id !=
                    $stav_riesenia->typ_stavu_riesenia_problemu_id) {

                    $key = $final->search(function ($item) use ($id) {
                        return $item->problem_id == $id;
                    });

                    $final->pull($key);
                }
            }
        }

        if ($request->vozidlo_id != null) {
            foreach ($final as $problem) {

                $id = $problem->problem_id;

                $vozidlo = DB::table('priradene_vozidlo')
                    ->where('problem_id', '=', $problem->problem_id)
                    ->latest('priradene_vozidlo_id')->first();


                if ($vozidlo != null) {
                    if ($request->vozidlo_id != $vozidlo->vozidlo_id) {
                        $key = $final->search(function ($item) use ($id) {
                            return $item->problem_id == $id;
                        });
                        $final->pull($key);
                    }
                } else {
                    $key = $final->search(function ($item) use ($id) {
                        return $item->problem_id == $id;
                    });
                    $final->pull($key);
                }
            }
        }


        if ($request->zamestnanec_id != null) {
            foreach ($final as $problem) {

                $id = $problem->problem_id;

                $zamestnanec = DB::table('priradeny_zamestnanec')
                    ->where('problem_id', '=', $problem->problem_id)
                    ->latest('priradeny_zamestnanec_id')->first();


                if ($zamestnanec != null) {
                    if ($request->zamestnanec_id != $zamestnanec->zamestnanec_id) {
                        $key = $final->search(function ($item) use ($id) {
                            return $item->problem_id == $id;
                        });
                        $final->pull($key);
                    }
                } else {
                    $key = $final->search(function ($item) use ($id) {
                        return $item->problem_id == $id;
                    });
                    $final->pull($key);
                }
            }
        }

        $all = Problem::all();
        if($all->count() == $final->count()){
            return redirect('/problem');
        }

        $rola = Auth::user()->rola_id;
        $user_id = Auth::user()->id;
        $typy_stavov = TypStavuRieseniaProblemu::all();
        $users = User::all();
        $vsetky_vozidla = Vozidlo::all();
        $stavy_riesenia = array();
        $priradeni_zamestnanci = array();
        $priradene_vozidla = array();

        $kategorie = KategoriaProblemu::all();
        $stavyProblemu = StavProblemu::all();
        $typySTavovRieseniaProblemu = TypStavuRieseniaProblemu::all();
        $priority = Priorita::all();
        $zamestnanciAll = User::where('rola_id', '>', '2')->get();

        foreach ($final as $problem) {
            $typ = DB::table('stav_riesenia_problemu')
                ->where('problem_id', '=', $problem->problem_id)
                ->latest('stav_riesenia_problemu_id')->first();
            array_push($stavy_riesenia, $typ->typ_stavu_riesenia_problemu_id);

            $zamestnanci = DB::table('priradeny_zamestnanec')
                ->where('problem_id', '=', $problem->problem_id)
                ->latest('priradeny_zamestnanec_id')->first();
            if ($zamestnanci != null)
                array_push($priradeni_zamestnanci, $zamestnanci->zamestnanec_id);
            else
                array_push($priradeni_zamestnanci, 0);

            $vozidla = DB::table('priradene_vozidlo')
                ->where('problem_id', '=', $problem->problem_id)
                ->latest('priradene_vozidlo_id')->first();


            if ($vozidla != null)
                array_push($priradene_vozidla, $vozidla->vozidlo_id);
            else
                array_push($priradene_vozidla, 0);

        }


        if ($rola == 3)
            return view('problem.admin.admin_index')
                ->with('problems', $final)
                ->with('stavy_riesenia', $stavy_riesenia)
                ->with('typy_stavov_riesenia', $typy_stavov)
                ->with('priradeni_zamestnanci', $priradeni_zamestnanci)
                ->with('zamestnanci', $users)
                ->with('priradene_vozidla', $priradene_vozidla)
                ->with('vozidla', $vsetky_vozidla)
                ->with('kategorie', $kategorie)
                ->with('stavyProblemu', $stavyProblemu)
                ->with('typyStavovRieseniaProblemu', $typySTavovRieseniaProblemu)
                ->with('priority', $priority)
                ->with('VsetciZamestnanci', $zamestnanciAll);
        if ($rola == 4)
            return view('problem.dispecer.dispecer_index')
                ->with('problems', $final)
                ->with('stavy_riesenia', $stavy_riesenia)
                ->with('typy_stavov_riesenia', $typy_stavov)
                ->with('priradeni_zamestnanci', $priradeni_zamestnanci)
                ->with('zamestnanci', $users)
                ->with('priradene_vozidla', $priradene_vozidla)
                ->with('vozidla', $vsetky_vozidla)
                ->with('kategorie', $kategorie)
                ->with('stavyProblemu', $stavyProblemu)
                ->with('typyStavovRieseniaProblemu', $typySTavovRieseniaProblemu)
                ->with('priority', $priority)
                ->with('VsetciZamestnanci', $zamestnanciAll);
        if ($rola == 5)
            return view('problem.manazer.manazer_index')
                ->with('problems', $final)
                ->with('stavy_riesenia', $stavy_riesenia)
                ->with('typy_stavov_riesenia', $typy_stavov)
                ->with('priradeni_zamestnanci', $priradeni_zamestnanci)
                ->with('zamestnanci', $users)
                ->with('priradene_vozidla', $priradene_vozidla)
                ->with('vozidla', $vsetky_vozidla)
                ->with('kategorie', $kategorie)
                ->with('stavyProblemu', $stavyProblemu)
                ->with('typyStavovRieseniaProblemu', $typySTavovRieseniaProblemu)
                ->with('priority', $priority)
                ->with('VsetciZamestnanci', $zamestnanciAll);

    }


    /**
     * mapa vsetkych hlaseni pre pouzivatelov
     * @return \Illuminate\Contracts\Foundation\Application|\Illuminate\Contracts\View\Factory|\Illuminate\View\View
     *
     *
     * obsahuje infowindows s detailnymi info o jednotlivych problemoch
     */
    public function mapa()
    {


        $rola = Auth::user()->rola_id;
        $problems = Problem::all();
        $typy_stavov_riesenia = TypStavuRieseniaProblemu::all();
        $popisyAll = PopisStavuRieseniaProblemu::all();
        $usersAll = User::all();
        $vozidlaAll = Vozidlo::all();


        $stavy_riesenia = array();
        $popisy_stavov_riesenia = array();
        $zamestnanciArr = array();
        $vozidlaArr = array();

        $counter = 0;

        foreach ($problems as $problem) {
            $typ = DB::table('stav_riesenia_problemu')
                ->where('problem_id', '=', $problem->problem_id)
                ->latest('stav_riesenia_problemu_id')->first();
            array_push($stavy_riesenia, $typ->typ_stavu_riesenia_problemu_id);

            $popisTable = DB::table('popis_stavu_riesenia_problemu')
                ->where('problem_id', '=', $problem->problem_id)
                ->latest('popis_stavu_riesenia_problemu_id')->first();
            if ($popisTable != null) {
                if ($popisTable->popis_stavu_riesenia_problemu_id != null)
                    $id = $popisTable->popis_stavu_riesenia_problemu_id;

                array_push($popisy_stavov_riesenia, $id);
            } else array_push($popisy_stavov_riesenia, 0);


            $priradeni_zamestnanci = DB::table('priradeny_zamestnanec')
                ->where('problem_id', '=', $problem->problem_id)
                ->latest('priradeny_zamestnanec_id')->first();
            if ($priradeni_zamestnanci != null) {
                if ($priradeni_zamestnanci->zamestnanec_id != null)
                    $id = $priradeni_zamestnanci->zamestnanec_id;

                array_push($zamestnanciArr, $id);
            } else array_push($zamestnanciArr, 0);

            $priradene_vozidla = DB::table('priradene_vozidlo')
                ->where('problem_id', '=', $problem->problem_id)
                ->latest('priradene_vozidlo_id')->first();
            if ($priradene_vozidla != null) {
                if ($priradene_vozidla->vozidlo_id != null)
                    $id = $priradene_vozidla->vozidlo_id;

                array_push($vozidlaArr, $id);
            } else array_push($vozidlaArr, 0);
        }

        if ($rola == 1) {
            return view('problem.obcan.obcan_mapa_problemov')
                ->with('problems', $problems)
                ->with('typy_stavov_riesenia', $typy_stavov_riesenia)
                ->with('stavy_riesenia', $stavy_riesenia)
                ->with('popisyAll', $popisyAll)
                ->with('popisyArr', $popisy_stavov_riesenia)
                ->with('counter', $counter);
        } else if ($rola == 3) {
            return view('problem.admin.admin_mapa_problemov')
                ->with('problems', $problems)
                ->with('typy_stavov_riesenia', $typy_stavov_riesenia)
                ->with('stavy_riesenia', $stavy_riesenia)
                ->with('popisyAll', $popisyAll)
                ->with('popisyArr', $popisy_stavov_riesenia)
                ->with('counter', $counter)
                ->with('usersAll', $usersAll)
                ->with('zamestnanciArr', $zamestnanciArr)
                ->with('vozidlaAll', $vozidlaAll)
                ->with('vozidlaArr', $vozidlaArr);
        } else if ($rola == 4) {
            return view('problem.dispecer.dispecer_mapa_problemov')
                ->with('problems', $problems)
                ->with('typy_stavov_riesenia', $typy_stavov_riesenia)
                ->with('stavy_riesenia', $stavy_riesenia)
                ->with('popisyAll', $popisyAll)
                ->with('popisyArr', $popisy_stavov_riesenia)
                ->with('counter', $counter)
                ->with('usersAll', $usersAll)
                ->with('zamestnanciArr', $zamestnanciArr)
                ->with('vozidlaAll', $vozidlaAll)
                ->with('vozidlaArr', $vozidlaArr);
        } else if ($rola == 5) {
            return view('problem.manazer.manazer_mapa_problemov')
                ->with('problems', $problems)
                ->with('typy_stavov_riesenia', $typy_stavov_riesenia)
                ->with('stavy_riesenia', $stavy_riesenia)
                ->with('popisyAll', $popisyAll)
                ->with('popisyArr', $popisy_stavov_riesenia)
                ->with('counter', $counter)
                ->with('usersAll', $usersAll)
                ->with('zamestnanciArr', $zamestnanciArr)
                ->with('vozidlaAll', $vozidlaAll)
                ->with('vozidlaArr', $vozidlaArr);
        }
    }

    public function welcomePage()
    {
        $problems = Problem::all();
        $typy_stavov_riesenia = TypStavuRieseniaProblemu::all();
        $popisyAll = PopisStavuRieseniaProblemu::all();


        $stavy_riesenia = array();
        $popisy_stavov_riesenia = array();

        $counter = 0;

        foreach ($problems as $problem) {
            $typ = DB::table('stav_riesenia_problemu')
                ->where('problem_id', '=', $problem->problem_id)
                ->latest('stav_riesenia_problemu_id')->first();
            array_push($stavy_riesenia, $typ->typ_stavu_riesenia_problemu_id);

            $popisTable = DB::table('popis_stavu_riesenia_problemu')
                ->where('problem_id', '=', $problem->problem_id)
                ->latest('popis_stavu_riesenia_problemu_id')->first();
            if ($popisTable != null) {
                if ($popisTable->popis_stavu_riesenia_problemu_id != null)
                    $id = $popisTable->popis_stavu_riesenia_problemu_id;

                array_push($popisy_stavov_riesenia, $id);
            } else array_push($popisy_stavov_riesenia, 0);
        }

        return view('welcomePage')
            ->with('problems', $problems)
            ->with('typy_stavov_riesenia', $typy_stavov_riesenia)
            ->with('stavy_riesenia', $stavy_riesenia)
            ->with('popisyAll', $popisyAll)
            ->with('popisyArr', $popisy_stavov_riesenia)
            ->with('counter', $counter);
    }



    //PRIDAL SIMON DRIENIK, ZOBRAZENIE VSETKYCH MARKEROV V ANDROID APPKE
    public function showAllProblemsAndroid()
    {
            $problems = Problem::all();
            $arr = array(); 
            //$data = array();
            //$typy_stavov_problemov = StavProblemu::all();
            //$kategorie_problemu = KategoriaProblemu::all();
           // $stav_riesenia_problemu = StavRieseniaProblemu::all();
            //$typ_stavu_riesenia_problemu1 = TypStavuRieseniaProblemu::all();
        //$popis_stavu_riesenia_problemu1 = PopisStavuRieseniaProblemu::all();
        $poc = 0; 

       /* $data['problems'] = $problems;
            $data['typy_stavov_problemov'] = $typy_stavov_problemov;
            $data['kategorie_problemu'] = $kategorie_problemu;
            $data['stav_riesenia_problemu'] = $stav_riesenia_problemu;
            $data['typ_stavu_riesenia_problemu1'] = $typ_stavu_riesenia_problemu1;
        $data['popis_stavu_riesenia_problemu1'] = $popis_stavu_riesenia_problemu1;*/

           /* var typ_stavu_problemu_nazov;
            var stav_riesenia_problemu_id;
            var kategoria_problemu_nazov;
            var typ_stavu_riesenia_problemu_nazov;
            var popis_stavu_riesenia_problemu_nazov;
        var poc = 0;*/

            foreach($problems as $problem)
        {
            $stav_problemu = DB::table('stav_problemu')
                ->where('stav_problemu_id', '=', $problem->stav_problemu_id)
                ->pluck('nazov')
                ->first();
            $stav_problemu = iconv('UTF-8', 'ASCII//TRANSLIT', $stav_problemu);

            $kategoria_problemu = DB::table('kategoria_problemu')
                ->where('kategoria_problemu_id', '=', $problem->kategoria_problemu_id)
                ->pluck('nazov')
                ->first();
            $kategoria_problemu = iconv('UTF-8', 'ASCII//TRANSLIT', $kategoria_problemu);

            $stav_riesenia_problemu = DB::table('stav_riesenia_problemu')
                ->where('problem_id', '=', $problem->problem_id)
                ->pluck('typ_stavu_riesenia_problemu_id')
                ->first();
            $typ_stavu_riesenia_problemu = DB::table('typ_stavu_riesenia_problemu')
                ->where('typ_stavu_riesenia_problemu_id', '=', $stav_riesenia_problemu)
                ->pluck('nazov')
                ->first();
            $typ_stavu_riesenia_problemu = iconv('UTF-8', 'ASCII//TRANSLIT', $typ_stavu_riesenia_problemu);

            $popis_stavu_riesenia_problemu = DB::table('popis_stavu_riesenia_problemu')
                ->where('problem_id', '=', $problem->problem_id)
                ->pluck('popis')
                ->first();
            if ($popis_stavu_riesenia_problemu != null)
            {
                $popis_stavu_riesenia_problemu = iconv('UTF-8', 'ASCII//TRANSLIT', $popis_stavu_riesenia_problemu);
            }
            if ($popis_stavu_riesenia_problemu == null)
            {
             $popis_stavu_riesenia_problemu = 'neuvedene';   
            }     

           /* $kategoria_problemu = DB::table('kategoria_problemu')
                ->where('kategoria_problemu_id', '=', $problem->kategoria_problemu_id)->get();
            $kategoria_problemu_nazov = $kategoria_problemu->nazov;

            $stav_riesenia_problemu = DB::table('stav_riesenia_problemu')
                ->where('problem_id', '=', $problem->problem_id)->get();
            $stav_riesenia_problemu_id = $stav_riesenia_problemu->typ_stavu_riesenia_problemu_id;

            $typ_stavu_riesenia_problemu = DB::table('typ_stavu_riesenia_problemu')
                ->where('typ_stavu_riesenia_problemu_id', '=', $stav_riesenia_problemu_id)->get();
            $typ_stavu_riesenia_problemu_nazov = $typ_stavu_riesenia_problemu->nazov;

            $popis_stavu_riesenia_problemu = DB::table('popis_stavu_riesenia_problemu')
                ->where('problem_id', '=', $problem->problem_id)->get();
            $popis_stavu_riesenia_problemu_nazov = $popis_stavu_riesenia_problemu->popis;

           /* foreach ($typy_stavov_problemov as $typ_stavu_problemu)
            {
                if ($problem->stav_problemu_id == $typ_stavu_problemu->typ_problemu_id) {
                   $typ_stavu_problemu_nazov = $typ_stavu_problemu->nazov;
                }
            }

            foreach ($kategorie_problemu as $kategoria_problemu1)
            {
                if ($problem->kategoria_problemu_id == $kategoria_problemu1->kategoria_problemu_id) {
                   $kategoria_problemu_nazov = $kategoria_problemu1->nazov;
                }
            }

        foreach ($stav_riesenia_problemu as $stav_riesenia)
            {

                if ($stav_riesenia->problem_id == $problem->problem_id) {
                    $stav_riesenia_problemu_id = $stav_riesenia->typ_stavu_riesenia_problemu_id;
                }
            }

            foreach ($typ_stavu_riesenia_problemu1 as $typ_stavu_riesenia_problemu2)
            {

                if ($typ_stavu_riesenia_problemu2->typ_stavu_riesenia_problemu_id  == stav_riesenia_problemu_id) {
                    $typ_stavu_riesenia_problemu_nazov = $typ_stavu_riesenia_problemu2->nazov;
                }
            }

            foreach ($popis_stavu_riesenia_problemu1 as $popis_stavu_riesenia_problemu2)
            {

                if ($popis_stavu_riesenia_problemu2->problem_id == $problem->problem_id) {
                    $popis_stavu_riesenia_problemu_nazov = $popis_stavu_riesenia_problemu2->nazov;
                }
        }*/

       /* if ($typ_stavu_riesenia_problemu_nazov == null)
        {
            $typ_stavu_riesenia_problemu_nazov = 'neuvedene';
        }

        if ($popis_stavu_riesenia_problemu_nazov == null)
        {
            $popis_stavu_riesenia_problemu_nazov = 'neuvedene';
	}*/

	    
	    $created_at1 = $problem->created_at->toDateTimeString();
	    //$created_at1 = date('Y-m-d H:i:s', strtotime('+1 hour', strtotime($created_at)));


                $arr[$poc] = array( 
                    "position" => $problem->poloha,  
                    "id" => $problem->problem_id, 
                    "kategoria" => $kategoria_problemu,
                    "popis" => $problem->popis_problemu,
                    "stav_problemu" =>$stav_problemu,
                    "stav_riesenia_problemu" =>$typ_stavu_riesenia_problemu,  
            "popis_riesenia_problemu" =>$popis_stavu_riesenia_problemu,
            "created_at" => $created_at1
            );

                $poc++;

       }

       return json_encode($arr);
            //return view('AllProblemsAndroid', ['data' => $data]);
           

        //return $arr;
       //return view('AllProblemsAndroid')->with('problems', json_decode($problems, true));

    }

    public function unregisteredAddRecordAndroid($poloha, $popis_problemu, $kategoria_problemu, $stav_problemu)
    {
        $arr = array();
         
                    
            if ($poloha != null)
        {
        $kategoria_problemu_id = DB::table('kategoria_problemu')
            ->where('nazov', $kategoria_problemu)
            ->pluck('kategoria_problemu_id')
            ->first();

        $stav_problemu_id = DB::table('stav_problemu')
            ->where('nazov', $stav_problemu)
            ->pluck('stav_problemu_id')
            ->first();

       /* $id = DB::table('problem')->insertGetId(array(
         'priorita_id' => '0',
         'cesta_id' => '0',
         'pouzivatel_id' => '0', 
         'poloha' => $poloha,
             'popis_problemu' => $popis_problemu,
                 'kategoria_problemu_id' => $kategoria_problemu_id,
         'stav_problemu_id' => $stav_problemu_id         
        ));

        DB::table('stav_riesenia_problemu')->insert(array(
            'problem_id' => $id,
            'typ_stavu_riesenia_problemu_id' => 1
        ));

        DB::table('popis_stavu_riesenia_problemu')->insert(array(
            'problem_id' => $id
        ));
        }*/

    $arr = array(
        'pouzivatel_id' => '1',
            'poloha' => $poloha,
        'popis_problemu' => $popis_problemu,
        'kategoria_problemu_id' => $kategoria_problemu_id,
        'stav_problemu_id' => $stav_problemu_id     
    );

    
    Problem::create($arr);
    $last = DB::table('problem')->latest('problem_id')->first();
    StavRieseniaProblemu::create(['problem_id' => $last->problem_id, 'typ_stavu_riesenia_problemu_id' => 1]);

   
    }
 	
    }
    

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        $rola = Auth::user()->rola_id;
        $kategorie = KategoriaProblemu::all();
        $stavy = StavProblemu::all();

        if (($rola == 1) || ($rola == 2)) {

            return view('problem.obcan.obcan_create')->with('kategorie', $kategorie)->with('stavy', $stavy);
        } else if ($rola == 3) {
            return view('problem.admin.admin_create')->with('kategorie', $kategorie)->with('stavy', $stavy);
        } else if ($rola == 4) {
            return view('problem.dispecer.dispecer_create')->with('kategorie', $kategorie)->with('stavy', $stavy);
        } else if ($rola == 5) {
            return view('problem.manazer.manazer_create')->with('kategorie', $kategorie)->with('stavy', $stavy);
        }
    }

    public function welcomePageCreate()
    {
        $kategorie = KategoriaProblemu::all();
        $stavy = StavProblemu::all();
        return view('problem.nezaregistrovanyObcan.nezaregistrovanyObcan_create')
            ->with('kategorie', $kategorie)->with('stavy', $stavy);
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param \Illuminate\Http\Request $request
     * @return \Illuminate\Http\Response
     *
     * validacia povinnych polí
     * pridanie user id tvorcu problemu do requestu
     * vytvorenie zaznamu problemu v DB
     * pre kazdy vytvoreny problem, vytvorim zaznam v stav_riesenia_problemu s hodnotou prijate
     */
    public function store(Request $request)
    {

        $request->validate([
            'poloha' => 'required',
            'popis_problemu' => 'required'
        ]);


        $request->request->add(['pouzivatel_id' => Auth::user()->id]);


        Problem::create($request->all());

        $last = DB::table('problem')->latest('problem_id')->first();
        StavRieseniaProblemu::create(['problem_id' => $last->problem_id, 'typ_stavu_riesenia_problemu_id' => 1]);


        return redirect('problem');
        //->with('success', 'Hlasenie bolo prijate!');
    }

    public function welcomePageStore(Request $request)
    {
        $request->validate([
            'poloha' => 'required',
            'popis_problemu' => 'required'
        ]);

        $request->request->add(['pouzivatel_id' => '1']);
        Problem::create($request->all());

        $last = DB::table('problem')->latest('problem_id')->first();
        StavRieseniaProblemu::create(['problem_id' => $last->problem_id, 'typ_stavu_riesenia_problemu_id' => 1]);


        return redirect('/');
        //->with('success', 'Hlasenie bolo prijate!');

    }

    /**
     * Display the specified resource.
     *
     * @param int $id
     * @return \Illuminate\Http\Response
     */
    public function show(Problem $problem)
    {
        $rola = Auth::user()->rola_id;

        $typ = StavRieseniaProblemu::where('problem_id', '=', $problem->problem_id)
            ->latest('stav_riesenia_problemu_id')->first();
        $popis_riesenia = PopisStavuRieseniaProblemu::where('problem_id', '=', $problem->problem_id)
            ->latest('popis_stavu_riesenia_problemu_id')->first();


        if ($rola == 1) {
            return view('problem.obcan.obcan_detail', compact('problem', $problem))
                ->with('stav_riesenia_problemu', $typ)
                ->with('popis_stavu_riesenia', $popis_riesenia);
        } else {
            $zamestnanec = PriradenyZamestnanec::where('problem_id', '=', $problem->problem_id)
                ->latest('priradeny_zamestnanec_id')->first();

            $vozidlo = PriradeneVozidlo::where('problem_id', '=', $problem->problem_id)
                ->latest('priradene_vozidlo_id')->first();

            if ($rola == 3) {
                return view('problem.admin.admin_detail', compact('problem', $problem))
                    ->with('stav_riesenia_problemu', $typ)
                    ->with('popis_stavu_riesenia', $popis_riesenia)
                    ->with('priradeny_zamestnanec', $zamestnanec)
                    ->with('priradene_vozidlo', $vozidlo);
            }
            if ($rola == 4) {
                return view('problem.dispecer.dispecer_detail', compact('problem', $problem))
                    ->with('stav_riesenia_problemu', $typ)
                    ->with('popis_stavu_riesenia', $popis_riesenia)
                    ->with('priradeny_zamestnanec', $zamestnanec)
                    ->with('priradene_vozidlo', $vozidlo);
            }
            if ($rola == 5) {
                return view('problem.manazer.manazer_detail', compact('problem', $problem))
                    ->with('stav_riesenia_problemu', $typ)
                    ->with('popis_stavu_riesenia', $popis_riesenia)
                    ->with('priradeny_zamestnanec', $zamestnanec)
                    ->with('priradene_vozidlo', $vozidlo);
            }
        }

    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param int $id
     * @return \Illuminate\Http\Response
     */
    public function edit(Problem $problem)
    {
        $priority = Priorita::all();
        $kraje = Kraj::all();
        $katastralne_uzemia = KatastralneUzemie::all();
        $obce = Obec::all();
        $spravcovia = Spravca::all();
        $typy_stavov_riesenia_problemu = TypStavuRieseniaProblemu::all();
        $kategorie = KategoriaProblemu::all();
        $stavy_problemu = StavProblemu::all();
        $vozidla = Vozidlo::all();


        $priradeny_zamestanec = PriradenyZamestnanec::where('problem_id', '=', $problem->problem_id)
            ->latest('priradeny_zamestnanec_id')->first();

        $stav = StavRieseniaProblemu::where('problem_id', '=', $problem->problem_id)
            ->latest('stav_riesenia_problemu_id')->first();

        $priradene_vozidlo = PriradeneVozidlo::where('problem_id', '=', $problem->problem_id)
            ->latest('priradene_vozidlo_id')->first();

        $priradeny_popis_riesenia = PopisStavuRieseniaProblemu::where('problem_id', '=', $problem->problem_id)
            ->latest('popis_stavu_riesenia_problemu_id')->first();

        $dispeceri = DB::table('users')->where('rola_id', '=', 4)->get();

        $rola = Auth::user()->rola_id;

        if ($rola == 3) {
            return view('problem.admin.admin_edit', compact('problem', $problem))
                ->with('priority', $priority)
                ->with('kraje', $kraje)
                ->with('katastralne_uzemia', $katastralne_uzemia)
                ->with('obce', $obce)
                ->with('spravcovia', $spravcovia)
                ->with('typy_stavov_riesenia_problemu', $typy_stavov_riesenia_problemu)
                ->with('stav_riesenia_problemu', $stav)
                ->with('kategorie', $kategorie)
                ->with('vozidla', $vozidla)
                ->with('priradene_vozidlo', $priradene_vozidlo)
                ->with('stavy_problemu', $stavy_problemu)
                ->with('popis_stavu_riesenia', $priradeny_popis_riesenia)
                ->with('dispeceri', $dispeceri)
                ->with('priradeny_zamestnanec', $priradeny_zamestanec);
        } else if ($rola == 4) {
            return view('problem.dispecer.dispecer_edit', compact('problem', $problem))
                ->with('priority', $priority)
                ->with('kraje', $kraje)
                ->with('katastralne_uzemia', $katastralne_uzemia)
                ->with('obce', $obce)
                ->with('spravcovia', $spravcovia)
                ->with('typy_stavov_riesenia_problemu', $typy_stavov_riesenia_problemu)
                ->with('stav_riesenia_problemu', $stav)
                ->with('kategorie', $kategorie)
                ->with('vozidla', $vozidla)
                ->with('priradene_vozidlo', $priradene_vozidlo)
                ->with('stavy_problemu', $stavy_problemu)
                ->with('popis_stavu_riesenia', $priradeny_popis_riesenia)
                ->with('dispeceri', $dispeceri)
                ->with('priradeny_zamestnanec', $priradeny_zamestanec);
        } else if ($rola == 5) {
            return view('problem.manazer.manazer_edit', compact('problem', $problem))
                ->with('priority', $priority)
                ->with('kraje', $kraje)
                ->with('katastralne_uzemia', $katastralne_uzemia)
                ->with('obce', $obce)
                ->with('spravcovia', $spravcovia)
                ->with('typy_stavov_riesenia_problemu', $typy_stavov_riesenia_problemu)
                ->with('stav_riesenia_problemu', $stav)
                ->with('kategorie', $kategorie)
                ->with('vozidla', $vozidla)
                ->with('priradene_vozidlo', $priradene_vozidlo)
                ->with('stavy_problemu', $stavy_problemu)
                ->with('popis_stavu_riesenia', $priradeny_popis_riesenia)
                ->with('dispeceri', $dispeceri)
                ->with('priradeny_zamestnanec', $priradeny_zamestanec);
        }
    }

    /**
     * Update the specified resource in storage.
     *
     * @param \Illuminate\Http\Request $request
     * @param Problem $problem
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Problem $problem)
    {

        $stav = StavRieseniaProblemu::where('problem_id', '=', $problem->problem_id)
            ->latest('stav_riesenia_problemu_id')->first();
        $priradene_vozidlo = PriradeneVozidlo::where('problem_id', '=', $problem->problem_id)
            ->latest('priradene_vozidlo_id')->first();
        $aktualny_popis = PopisStavuRieseniaProblemu::where('problem_id', '=', $problem->problem_id)
            ->latest('popis_stavu_riesenia_problemu_id')->first();
        $aktualny_priradeny_zamestnanec = PriradenyZamestnanec::where('problem_id', '=', $problem->problem_id)
            ->latest('priradeny_zamestnanec_id')->first();


        if ($request->priorita_id != $problem->priorita_id) {
            $problem->priorita_id = $request->priorita_id;
        }
        if ($request->poloha != $problem->poloha) {
            $problem->poloha = $request->poloha;
        }
        if ($request->kategoria_problemu_id != $problem->kategoria_problemu_id) {
            $problem->kategoria_problemu_id = $request->kategoria;
        }
        if ($request->stav_problemu_id != $problem->stav_problemu_id) {
            $problem->stav_problemu_id = $request->stav_problemu_id;
        }
        if ($stav->typ_stavu_riesenia_problemu_id != $request->stav_riesenia_problemu_id) {
            StavRieseniaProblemu::create(['problem_id' => $problem->problem_id,
                'typ_stavu_riesenia_problemu_id' => $request->stav_riesenia_problemu_id]);
        }
        if ($request->priradene_vozidlo_id != 0) {
            if ($priradene_vozidlo == null) {
                PriradeneVozidlo::create(['problem_id' => $problem->problem_id,
                    'vozidlo_id' => $request->priradene_vozidlo_id]);
            } else
                if ($priradene_vozidlo->vozidlo_id != $request->priradene_vozidlo_id) {
                    PriradeneVozidlo::create(['problem_id' => $problem->problem_id,
                        'vozidlo_id' => $request->priradene_vozidlo_id]);
                }
        }
        if ($request->popis_stavu_riesenia_problemu != null) {
            if ($aktualny_popis == null) {
                PopisStavuRieseniaProblemu::create(['popis' => $request->popis_stavu_riesenia_problemu,
                    'problem_id' => $problem->problem_id]);
            } else
                if ($request->popis_stavu_riesenia_problemu != $aktualny_popis->popis) {
                    PopisStavuRieseniaProblemu::create(['popis' => $request->popis_stavu_riesenia_problemu,
                        'problem_id' => $problem->problem_id]);
                }
        }

        if ((Auth::user()->rola_id) == 5 || (Auth::user()->rola_id) == 3) {
            if ($request->priradeny_zamestnanec_id != null) {
                if ($request->priradeny_zamestnanec_id != 0) {
                    if ($aktualny_priradeny_zamestnanec == null) {
                        PriradenyZamestnanec::create(['zamestnanec_id' => $request->priradeny_zamestnanec_id,
                            'problem_id' => $problem->problem_id]);
                    } else
                        if ($request->priradeny_zamestnanec_id != $aktualny_priradeny_zamestnanec) {
                            PriradenyZamestnanec::create(['zamestnanec_id' => $request->priradeny_zamestnanec_id,
                                'problem_id' => $problem->problem_id]);
                        }
                }
            }
        }

        $problem->save();
        return redirect('problem');
    }


    /**
     * Remove the specified resource from storage.
     *
     * @param Problem $problem
     * @return \Illuminate\Http\Response
     */
    public function destroy(Problem $problem)
    {
        $problem = Problem::find($problem->problem_id);
        $problem->delete();

        return redirect('problem');
    }


    public function priradeneProblemyDispecerovi(User $user)
    {

        $dispecer = Auth::user()->id;
        $priradenyZamestnanec = PriradenyZamestnanec::where('zamestnanec_id', '=', $dispecer);


    }

    public function priradeniZamestnanci(Problem $problem)
    {
        $zamestnanci = PriradenyZamestnanec::where('problem_id', '=', $problem->problem_id)->get();

        $rola = Auth::user()->rola_id;

        if ($rola == 4) {
            return view('problem.dispecer.historia.dispecer_priradeniZamestnanci',
                compact('problem', $problem))->with('zamestnanci', $zamestnanci);
        } else if ($rola == 3) {
            return view('problem.admin.historia.admin_priradeniZamestnanci',
                compact('problem', $problem))->with('zamestnanci', $zamestnanci);
        } else if ($rola == 5) {
            return view('problem.manazer.historia.manazer_priradeniZamestnanci',
                compact('problem', $problem))->with('zamestnanci', $zamestnanci);
        }
    }

    public function priradeneVozidla(Problem $problem)
    {
        $vozidla = PriradeneVozidlo::where('problem_id', '=', $problem->problem_id)->get();

        $rola = Auth::user()->rola_id;

        if ($rola == 4) {
            return view('problem.dispecer.historia.dispecer_priradeneVozidla',
                compact('problem', $problem))->with('vozidla', $vozidla);
        } else if ($rola == 3) {
            return view('problem.admin.historia.admin_priradeneVozidla',
                compact('problem', $problem))->with('vozidla', $vozidla);
        } else if ($rola == 5) {
            return view('problem.manazer.historia.manazer_priradeneVozidla',
                compact('problem', $problem))->with('vozidla', $vozidla);
        }
    }

    public function stavyRieseniaProblemu(Problem $problem)
    {
        $stavy = StavRieseniaProblemu::where('problem_id', '=', $problem->problem_id)->get();

        $rola = Auth::user()->rola_id;

        if ($rola == 4) {
            return view('problem.dispecer.historia.dispecer_stavyRieseniaProblemu',
                compact('problem', $problem))->with('stavy', $stavy);
        } else if ($rola == 3) {
            return view('problem.admin.historia.admin_stavyRieseniaProblemu',
                compact('problem', $problem))->with('stavy', $stavy);
        } else if ($rola == 5) {
            return view('problem.manazer.historia.manazer_stavyRieseniaProblemu',
                compact('problem', $problem))->with('stavy', $stavy);
        }
    }

    public function popisyStavovRieseniaProblemu(Problem $problem)
    {
        $popisy = PopisStavuRieseniaProblemu::where('problem_id', '=', $problem->problem_id)->get();

        $rola = Auth::user()->rola_id;

        if ($rola == 4) {
            return view('problem.dispecer.historia.dispecer_popisyStavovRieseniaProblemu',
                compact('problem', $problem))->with('popisy', $popisy);
        } else if ($rola == 3) {
            return view('problem.admin.historia.admin_popisyStavovRieseniaProblemu',
                compact('problem', $problem))->with('popisy', $popisy);
        } else if ($rola == 5) {
            return view('problem.manazer.historia.manazer_popisyStavovRieseniaProblemu',
                compact('problem', $problem))->with('popisy', $popisy);
        }
    }
}
