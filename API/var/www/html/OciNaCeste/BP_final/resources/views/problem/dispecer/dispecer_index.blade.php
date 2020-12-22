@extends('custom_layout.dispecer.dispecer_app')
@section('content')

    @if (Session::has('message'))
        <div class="alert alert-info">{{ Session::get('message') }}</div>
    @endif

    <section class="main-container h-100">
        <div class="container-fluid">
            <div class="row">
                <div class="col-12">
                    <h1 class="text-center">Všetky hlásenia</h1>
                </div>





                <div class="col-12 d-flex justify-content-center flex-column">
                    <div class="table-responsive">
                        <table class="table main-table">
                            <thead>

                            <form action="{{ action('ProblemController@filter') }}"
                                  method="POST">
                                @csrf

                                <div class="col-12 d-flex justify-content-end mb-3">
                                    <button type="submit" class="btn btn-primary" name="submit">Filtruj</button>
                                </div>

                                <tr class="filter-row">
                                    <th scope="col"><p>#</p>
                                    </th>
                                    <th scope="col w-80"><p>ID</p></th>
                                    <th scope="col"><p>Poloha</p></th>
                                    <th scope="col"><p>Zadané</p>
                                        <select
                                            id="orderBy" class="input-filter form-input w-100"
                                            name="orderBy">

                                            <option value="" selected disabled hidden>Vyber</option>
                                            <option value="1" >Zoraď od najnovších</option>
                                            <option value="2" >Zoraď od najstarších</option>

                                        </select></th>


                                    <th scope="col"><p>Kategória</p><select
                                            id="kategoria_problemu_id" class="input-filter form-input w-100"
                                            name="kategoria_problemu_id">

                                            <option value="" selected disabled hidden>Vyber</option>
                                            @foreach($kategorie as $kategoria)
                                                <option value="{{ $kategoria->kategoria_problemu_id }}"
                                                >{{ $kategoria->nazov }}</option>
                                            @endforeach
                                        </select></th>


                                    <th scope="col"><p>Stav problému</p><select
                                            id="stav_problemu_id" class="input-filter form-input w-100"
                                            name="stav_problemu_id">
                                            <option value="" selected disabled hidden>Vyber</option>
                                            @foreach($stavyProblemu as $stav)
                                                <option value="{{ $stav->stav_problemu_id }}">
                                                    {{ $stav->nazov}}</option>
                                            @endforeach
                                        </select></th>

                                    <th scope="col"><p>Stav riešenia</p><select
                                            id="typ_stavu_riesenia_problemu" class="input-filter form-input w-100"
                                            name="typ_stavu_riesenia_problemu_id">
                                            <option value="" selected disabled hidden>Vyber</option>

                                            @foreach($typyStavovRieseniaProblemu as $stav)
                                                <option value="{{ $stav->typ_stavu_riesenia_problemu_id}}"
                                                >{{ $stav->nazov }}</option>
                                            @endforeach
                                        </select></th>

                                    <th scope="col"><p>Priorita</p><select
                                            id="priorita" class="input-filter form-input form-input w-100"
                                            name="priorita_id">
                                            <option value="" selected disabled hidden>Vyber</option>
                                            @foreach($priority as $priorita)
                                                <option value="{{ $priorita->priorita_id }}"
                                                >{{ $priorita->priorita }}</option>
                                            @endforeach
                                        </select></th>

                                    <th scope="col"><p>Používateľ</p>
                                        <select
                                            id="user" class="input-filter form-input form-input w-100"
                                            name="pouzivatel_id">
                                            <option value="" selected disabled hidden>Vyber</option>
                                            @foreach($zamestnanci as $user)
                                                <option value="{{ $user->id }}"
                                                >{{ $user->name }}</option>
                                            @endforeach
                                        </select>
                                    </th>
                                    <th scope="col"><p>Zamestnanec</p>
                                        <select
                                            id="user" class="input-filter form-input form-input w-100"
                                            name="zamestnanec_id">
                                            <option value="" selected disabled hidden>Vyber</option>
                                            @foreach($VsetciZamestnanci as $user)
                                                <option value="{{ $user->id }}"
                                                >{{ $user->name }}</option>
                                            @endforeach
                                        </select>
                                    </th>
                                    <th scope="col"><p>Vozidlo</p><select
                                            id="vozidla" class="input-filter form-input w-100"
                                            name="vozidlo_id">
                                            <option value="" selected disabled hidden>Vyber</option>
                                            @foreach($vozidla as $vozidlo)
                                                <option value="{{ $vozidlo->vozidlo_id }}"
                                                >{{ $vozidlo->SPZ }}</option>
                                            @endforeach
                                        </select></th>
                                    <th scope="col">Edit</th>
                                    <th scope="col">Detail</th>
                                    <th scope="col">Vymaž záznam</th>

                                </tr>
                            </form>
                            </thead>
                            <tbody>
                            @php
                                $counter=1;
                            @endphp
                            @foreach($problems as $problem)

                                <tr>
                                    <td>{{ $counter }}</td>
                                    <td class="w-80">{{ $problem->problem_id }}</td>
                                    <td>{{ $problem->poloha }}</td>
                                    <td>{{ $problem->created_at }}</td>
                                    <td>{{ $problem->KategoriaProblemu['nazov'] }}</td>
                                    <td>{{ $problem->StavProblemu['nazov'] }}</td>

                                    @foreach($typy_stavov_riesenia as $typ)
                                        @if($stavy_riesenia[$counter-1] == $typ->typ_stavu_riesenia_problemu_id)
                                            <td>{{ $typ->nazov }}</td>
                                        @endif
                                    @endforeach

                                    <td>{{ $problem->Priorita['priorita'] }}</td>
                                    <td>{{ $problem->users['name'] }}</td>

                                    @foreach($zamestnanci as $zamestnanec)
                                        @if($priradeni_zamestnanci[$counter-1] == 0)
                                            <td>Nepriradený</td>
                                            @break
                                        @else
                                            @if($priradeni_zamestnanci[$counter-1] == $zamestnanec->id)
                                                <td>{{ $zamestnanec->name }}</td>
                                            @endif
                                        @endif
                                    @endforeach



                                    @foreach($vozidla as $vozidlo)
                                        @if($priradene_vozidla[$counter-1] == 0)
                                            <td>Nepriradené</td>
                                            @break
                                        @else
                                            @if($priradene_vozidla[$counter-1] == $vozidlo->vozidlo_id)
                                                <td>{{ $vozidlo->SPZ }}</td>
                                            @endif
                                        @endif
                                    @endforeach


                                    <td><a href="/problem/{{ $problem->problem_id }}/edit" class="c-black"><i
                                                class="fas fa-edit"></i></a></td>
                                    <td><a href="/problem/{{ $problem->problem_id }}" class="c-black"><i
                                                class="fas fa-info"></i></a></td>
                                    <td>
                                        <p class="text-center">
                                            <button type="submit" class="border-0" data-toggle="modal"
                                                    data-target="#delete-modal-{{ $problem->problem_id }}"><i
                                                    class="far fa-trash-alt"></i>
                                            </button>
                                        </p>
                                    </td>
                                </tr>
                                @php
                                    $counter++;
                                @endphp

                                <div id="delete-modal-{{ $problem->problem_id }}" class="modal delete-modal"
                                     tabindex="-1"
                                     role="dialog">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title">Vymazať záznam</h5>
                                            </div>
                                            <div class="modal-body">

                                                <p>Ste si istý, že chcete vymazať záznam?</p>
                                                <ul class="d-flex align-items-center justify-content-center mt-4">
                                                    <li>
                                                        <button type="button" class="btn btn-primary cancel mr-4"
                                                                data-dismiss="modal"
                                                                aria-label="Close">Zrušiť
                                                        </button>
                                                    </li>
                                                    <li>
                                                        <form
                                                            action="{{ route('problem.destroy', $problem->problem_id) }}"
                                                            method="POST">
                                                            @method('DELETE')
                                                            @csrf
                                                            <p class="text-center">
                                                                <button type="submit" class="btn btn-danger delete">
                                                                    Vymazať
                                                                </button>
                                                            </p>

                                                        </form>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            @endforeach

                            </tbody>
                        </table>
                        @if(Request::url() === 'http://127.0.0.1:8000/problem')
                            {{$problems->links()}}
                        @endif
                    </div>


                    @if(!empty(Session::get('success')))
                        <div class="alert alert-success"> {{ Session::get('success') }}</div>
                    @endif
                    @if(!empty(Session::get('error')))
                        <div class="alert alert-danger"> {{ Session::get('error') }}</div>
                    @endif

                </div>
            </div>
        </div>

    </section>
@endsection
