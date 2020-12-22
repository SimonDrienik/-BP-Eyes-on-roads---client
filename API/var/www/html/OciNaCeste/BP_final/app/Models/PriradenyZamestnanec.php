<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class PriradenyZamestnanec extends Model
{
    protected $fillable = ['priradeny_zamestnanec_id', 'zamestnanec_id', 'problem_id'];

    protected $table='priradeny_zamestnanec';
    protected $primaryKey ='priradeny_zamestnanec_id';

    public function users(){
        return $this->belongsTo('App\User', 'zamestnanec_id');
    }
    public function Problem(){
        return $this->HasMany('App\Models\Problem', 'problem_id');
    }
}
