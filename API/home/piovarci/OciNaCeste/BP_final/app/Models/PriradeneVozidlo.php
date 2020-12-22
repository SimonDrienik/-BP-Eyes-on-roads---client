<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class PriradeneVozidlo extends Model
{
    protected $fillable = ['priradene_vozidlo_id', 'problem_id','vozidlo_id'];

    protected $table='priradene_vozidlo';
    protected $primaryKey ='priradene_vozidlo_id';

    public function Vozidlo(){
        return $this->belongsTo('App\Models\Vozidlo', 'vozidlo_id');
    }
    public function Problem(){
        return $this->HasMany('App\Models\Problem', 'problem_id');
    }
}
