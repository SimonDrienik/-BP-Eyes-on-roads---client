@extends('custom_layout.admin.admin_app')

@section('content')

    @if (Session::has('message'))
        <div class="alert alert-info">{{ Session::get('message') }}</div>
    @endif

    <div class="container">

        <form action="{{ route('problem.update', $problem->problem_id) }}" method="POST" class="row w-100">
            @csrf
            @method('PUT')
            <div class="col-12 col-sm-12 col-md-6">
                <ul class="detail update">
                    <li id="id"><label class="update-label">ID: </label><span>{{ $problem->problem_id }}</span></li>

                    <li id="created_at"><label class="update-label">Datum
                            vytvorenia: </label><span>{{ $problem->created_at }}</span></li>

                    <li id="user"><label
                            class="update-label">Vytvorene: </label><span>{{ $problem->users['name'] }}</span></li>

                    <li id="popis_problemu"><label class="update-label">Popis
                            problemu: </label><span>{{ $problem->popis_problemu }}</span></li>


                    <li id="priradeny_zamestnanec_id">
                        <label class="update-label">Priradený zamestnanec:</label>
                        <select id="priradeny_zamestnanec_id" class="form-select form-input" name="priradeny_zamestnanec_id">
                            @if($priradeny_zamestnanec == null)
                                <option value="0"
                                        selected>Nepriradený
                                </option>

                                @foreach($dispeceri as $dispecer)
                                    <option value="{{ $dispecer->id }}">{{ $dispecer->name }}</option>
                                @endforeach
                            @else

                                @foreach($dispeceri as $dispecer)
                                    @if($priradeny_zamestnanec != null)
                                        @if($dispecer->id == $priradeny_zamestnanec->zamestnanec_id)

                                            <option value="{{ $dispecer->id }}"
                                                    selected>{{ $dispecer->name }}</option>

                                        @else
                                            <option value="{{ $dispecer->id }}">{{ $dispecer->name }}</option>
                                        @endif

                                    @endif
                                @endforeach
                            @endif
                        </select>


                    <li id="priorita_id">
                        <label class="update-label">Priorita:</label>
                        <select id="priorita_id" class="form-select form-input" name="priorita_id">
                            @foreach($priority as $priorita)

                                @if($priorita->priorita_id == $problem->Priorita['priorita_id'])
                                    <option value="{{ $priorita->priorita_id }}"
                                            selected>{{ $priorita->priorita }}</option>

                                @else
                                    <option value="{{ $priorita->priorita_id }}">{{ $priorita->priorita }}</option>
                                @endif
                            @endforeach
                        </select>
                    </li>


                <!--
                        <li id="kraj">
                            <label class="update-label">Kraj:</label>
                            <select id="kraj" class="form-select form-input" name="kraj">
                                @foreach($kraje as $kraj)

                    @if($kraj->kraj_id == $problem->Cesta->Kraj['kraj_id'])
                        <option value="{{ $kraj->kraj_id }}"
                                                selected>{{ $kraj->nazov }}</option>

                                    @else
                        <option value="{{ $kraj->kraj_id }}">{{ $kraj->nazov }}</option>
                                    @endif
                @endforeach
                    </select>
                </li>

                <li id="katastralneUzemie">
                    <label class="update-label">Katastralne uzemie:</label>
                    <select id="katastralneUzemie" class="form-select form-input" name="katastralneUzemie">
@foreach($katastralne_uzemia as $uzemie)

                    @if($uzemie->katastralne_uzemie_id == $problem->Cesta->KatastralneUzemie['katastralne_uzemie_id'])
                        <option value="{{ $uzemie->katastralne_uzemie_id }}"
                                                selected>{{ $uzemie->nazov }}</option>

                                    @else
                        <option
                            value="{{ $uzemie->katastralne_uzemie_id }}">{{ $uzemie->nazov }}</option>
                                    @endif
                @endforeach
                    </select>
                </li>


                <li id="obec">
                    <label class="update-label">Obec:</label>
                    <select id="obec" class="form-select form-input" name="obec">
@foreach($obce as $obec)

                    @if($obec->obec_id == $problem->Cesta->Obec['obec_id'])
                        <option value="{{ $obec->obec_id }}"
                                                selected>{{ $obec->nazov }}</option>

                                    @else
                        <option value="{{ $obec->obec_id }}">{{ $obec->nazov }}</option>
                                    @endif
                @endforeach
                    </select>
                </li>

                <li id="spravca">
                    <label class="update-label">Spravca komunikacie:</label>
                    <select id="spravca" class="form-select form-input" name="spravca">
@foreach($spravcovia as $spravca)

                    @if($spravca->spravca_id == $problem->Cesta->Spravca['spravca_id'])
                        <option value="{{ $spravca->spravca_id }}"
                                                selected>{{ $spravca->nazov }}</option>

                                    @else
                        <option value="{{ $spravca->spravca_id }}">{{ $spravca->nazov }}</option>
                                    @endif
                @endforeach
                    </select>
                </li>
-->
                </ul>
            </div>


            <div class="col-12 col-sm-12 col-md-6">
                <ul class="detail update">

                    <!--  <li id="usek"><p class="detail-text"><span>Usek: </span>!!dorobit!!</p></li> -->

                    <li id="poloha"><label class="update-label">Poloha:</label><input type="text" class="form-input"
                                                                                      value="{{ $problem->poloha }}"
                                                                                      name="poloha">
                    </li>

                    <li id="kategoria_problemu_id">
                        <label class="update-label">Kategória:</label>
                        <select id="kategoria_problemu_id" class="form-select form-input" name="kategoria">
                            @foreach($kategorie as $kategoria)

                                @if($kategoria->kategoria_problemu_id == $problem->kategoria_problemu_id)

                                    <option value="{{ $kategoria->kategoria_problemu_id }}"
                                            selected>{{ $kategoria->nazov }}</option>

                                @else
                                    <option
                                        value="{{ $kategoria->kategoria_problemu_id }}">{{ $kategoria->nazov }}</option>
                                @endif
                            @endforeach
                        </select>

                    <li id="stav_problemu_id">
                        <label class="update-label">Stav problemu:</label>
                        <select id="stav_problemu_id" class="form-select form-input" name="stav_problemu_id">


                            @foreach($stavy_problemu as $stav)

                                @if($stav->stav_problemu_id == $problem->stav_problemu_id)

                                    <option value="{{ $stav->stav_problemu_id }}"
                                            selected>{{ $stav->nazov }}</option>

                                @else
                                    <option value="{{ $stav->stav_problemu_id }}">{{ $stav->nazov }}</option>
                                @endif
                            @endforeach

                        </select>

                    <li id="stav_riesenia_problemu_id">
                        <label class="update-label">Stav riesenia problemu:</label>
                        <select id="stav_riesenia_problemu_id" class="form-select form-input"
                                name="stav_riesenia_problemu_id">

                            @foreach($typy_stavov_riesenia_problemu as $typ)

                                @if($typ->typ_stavu_riesenia_problemu_id == $stav_riesenia_problemu->typ_stavu_riesenia_problemu_id)
                                    <option value="{{ $typ->typ_stavu_riesenia_problemu_id }}"
                                            selected>{{ $typ->nazov }}</option>

                                @else
                                    <option
                                        value="{{ $typ->typ_stavu_riesenia_problemu_id }}">{{ $typ->nazov }}</option>
                                @endif
                            @endforeach
                        </select>
                    </li>


                    <li id="priradene_vozidlo_id">
                        <label class="update-label">Priradené vozidlo:</label>
                        <select id="priradene_vozidlo_id" class="form-select form-input" name="priradene_vozidlo_id">
                            @if($priradene_vozidlo == null)
                                <option value="0"
                                        selected>Nepriradené
                                </option>

                                @foreach($vozidla as $vozidlo)
                                    <option value="{{ $vozidlo->vozidlo_id }}">{{ $vozidlo->SPZ }}</option>
                                @endforeach
                            @else

                                @foreach($vozidla as $vozidlo)
                                    @if($priradene_vozidlo != null)
                                        @if($vozidlo->vozidlo_id == $priradene_vozidlo->vozidlo_id)

                                            <option value="{{ $vozidlo->vozidlo_id }}"
                                                    selected>{{ $vozidlo->SPZ }}</option>

                                        @else
                                            <option value="{{ $vozidlo->vozidlo_id }}">{{ $vozidlo->SPZ }}</option>
                                        @endif

                                    @endif
                                @endforeach
                            @endif
                        </select>


                    <li id="popisRieseniaProblemu">
                        <label class="update-label">Popis riesenia problemu:</label>
                        @if( $popis_stavu_riesenia == null)
                            <label class="update-label">(zatiaľ nebol vytvorený pre daný problém)</label>

                            <textarea id="popisRieseniaProblemu" rows="6" class="form-input"
                                      name="popis_stavu_riesenia_problemu"></textarea>
                        @else

                            <textarea id="popisRieseniaProblemu" rows="6" class="form-input"
                                      name="popis_stavu_riesenia_problemu">{{ $popis_stavu_riesenia->popis }}</textarea>

                        @endif
                    </li>


                </ul>
            </div>
            <div class="col-12 mt-4">
                <!-- tabulka s mojimi hlaseniami -->
                <div class="d-flex align-items-center justify-content-center">
                    <button type="submit" class="btn btn-primary update-btn mr-3">Aktualizovať</button>

                    <button type="button" class="btn btn-danger remove-btn" data-toggle="modal"
                            data-target="#delete-modal-{{ $problem->problem_id }}">Vymazať
                    </button>
                </div>

            </div>
        </form>
    </div>



    <div id="delete-modal-{{ $problem->problem_id }}" class="modal delete-modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Vymazať záznam</h5>
                </div>
                <div class="modal-body">

                    <p>Ste si istý, že chcete vymazať záznam?</p>
                    <ul class="d-flex align-items-center justify-content-center mt-4">
                        <li>
                            <button type="button" class="btn btn-primary cancel mr-4" data-dismiss="modal"
                                    aria-label="Close">Zrušiť
                            </button>
                        </li>
                        <li>
                            <form action="{{ route('problem.destroy', $problem->problem_id) }}" method="POST">
                                @method('DELETE')
                                @csrf
                                <p class="text-center">
                                    <button type="submit" class="btn btn-danger delete">Vymazať</button>
                                </p>

                            </form>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    @if(!empty(Session::get('success')))
        <div class="alert alert-success"> {{ Session::get('success') }}</div>
    @endif
    @if(!empty(Session::get('error')))
        <div class="alert alert-danger"> {{ Session::get('error') }}</div>
    @endif

@endsection
