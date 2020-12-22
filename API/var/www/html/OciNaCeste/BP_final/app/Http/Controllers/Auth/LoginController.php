<?php

namespace App\Http\Controllers\Auth;

use App\Http\Controllers\Controller;
use App\Providers\RouteServiceProvider;
use Illuminate\Foundation\Auth\AuthenticatesUsers;
use Illuminate\Support\Facades\Auth;


class LoginController extends Controller
{
    /*
    |--------------------------------------------------------------------------
    | Login Controller
    |--------------------------------------------------------------------------
    |
    | This controller handles authenticating users for the application and
    | redirecting them to your home screen. The controller uses a trait
    | to conveniently provide its functionality to your applications.
    |
    */

    use AuthenticatesUsers;

    /**
     * Where to redirect users after login.
     *
     * @var string
     */

    protected $redirectTo;
    public function redirectTo(){
        $id = Auth::user()->rola_id;

        if($id == 1){
            $this->redirectTo = RouteServiceProvider::ZObcanHome;
            return $this->redirectTo;
        }
        if($id == 2){
            $this->redirectTo = RouteServiceProvider::NObcanHome;
            return $this->redirectTo;
        }
        if($id == 3){
            $this->redirectTo = RouteServiceProvider::AdminHome;
            return $this->redirectTo;
        }
        if($id == 4){
            $this->redirectTo = RouteServiceProvider::DispecerHome;
            return $this->redirectTo;
        }
        if($id == 5){
            $this->redirectTo = RouteServiceProvider::ManazerHome;
            return $this->redirectTo;
        }
    }



    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware('guest')->except('logout');
    }
}
