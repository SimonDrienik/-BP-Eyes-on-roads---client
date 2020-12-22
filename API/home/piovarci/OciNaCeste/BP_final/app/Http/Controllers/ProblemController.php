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

    public function index()
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
                    ->with('vozidla', $vsetky_vozidla);
            if ($rola == 4)
                return view('problem.dispecer.dispecer_index')
                    ->with('problems', $problems)
                    ->with('stavy_riesenia', $stavy_riesenia)
                    ->with('typy_stavov_riesenia', $typy_stavov)
                    ->with('priradeni_zamestnanci', $priradeni_zamestnanci)
                    ->with('zamestnanci', $users)
                    ->with('priradene_vozidla', $priradene_vozidla)
                    ->with('vozidla', $vsetky_vozidla);
            if ($rola == 5)
                return view('problem.manazer.manazer_index')
                    ->with('problems', $problems)
                    ->with('stavy_riesenia', $stavy_riesenia)
                    ->with('typy_stavov_riesenia', $typy_stavov)
                    ->with('priradeni_zamestnanci', $priradeni_zamestnanci)
                    ->with('zamestnanci', $users)
                    ->with('priradene_vozidla', $priradene_vozidla)
                    ->with('vozidla', $vsetky_vozidla);
        }
    }

    public function mapa()
    {

        $rola = Auth::user()->rola_id;
        $problems = Problem::all();

        if ($rola == 1) {
            return view('problem.obcan.obcan_mapa_problemov')->with('problems', $problems);
        } else if ($rola == 3) {
            return view('problem.admin.admin_mapa_problemov')->with('problems', $problems);
        } else if ($rola == 4) {
            return view('problem.dispecer.dispecer_mapa_problemov')->with('problems', $problems);
        } else if ($rola == 5) {
            return view('problem.manazer.manazer_mapa_problemov')->with('problems', $problems);
        }
    }

    public function welcomePage()
    {
        $problems = Problem::all();
        return view('welcomePage')->with('problems', $problems);
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

    public function welcomePageCreate(){
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

    public function welcomePageStore(Request $request){
        $request->validate([
            'poloha' => 'required',
            'popis_problemu' => 'required'
        ]);

        $request->request->add(['pouzivatel_id' => '0']);
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
