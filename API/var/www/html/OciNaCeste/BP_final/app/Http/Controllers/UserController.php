<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\User;
use App\Rola;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Redirect;

class UserController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $rola = Auth::user()->rola_id;

        if($rola == 3) {
            $users = User::all();
            $roles = Rola::all();
            return view('pouzivatelia.admin_index')->with('users', $users)->with('roly', $roles);
        }
        else if($rola == 5){

            //$users = User::where('rola_id', '4');
            //$users = User::all();
            $users = DB::table('users')->where('rola_id','4')->get();

            return view('pouzivatelia.manazer_index')->with('users', $users);
        }
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        //
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function edit($id)
    {

        $rola = Auth::user()->rola_id;

        if($rola == 1){
            return view('pouzivatelia.obcan_edit');
        }
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        $user = User::find($id);

        if($request->rola_id != null) {
            $user->rola_id = $request->rola_id;
            $user->save();
        }

        if($request->name != $user->name){
            $request->validate([
                'name' => ['required', 'min:5', 'string', 'max:255']
            ]);
        }


        if($request->email != $user->email){
            $request->validate([
                'email' => ['required', 'string', 'email', 'max:255', 'unique:users']
            ]);
        }

        if($request->password != null){
            $request->validate([
                'password' => ['required', 'string', 'min:8', 'confirmed'],
            ]);
        }

        $user->name = $request->name;
        $user->email = $request->email;

        if($request->password != null) {
            $user->password = Hash::make($request->password);
        }
        $user->save();



        if(Auth::user()->rola_id == 3)
            return redirect('pouzivatelia');
        else
            return redirect('problem');
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param User $user
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {

        $users = User::find($id);
        $users->delete();

        return redirect('pouzivatelia');
    }
}
