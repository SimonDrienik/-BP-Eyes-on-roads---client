<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Komentar extends Model
{
    protected $fillable = ['komentar_id', 'je_zamestnanec', 'komentar', 'problem_id', 'pouzivatel_id'];

    protected $table='komentar';
    protected $primaryKey ='komentar_id';

    public function users(){
        return $this->belongsTo('App\User', 'id');
    }

    public function Problem(){
        return $this->belongsTo('App\Models\Problem', 'problem_id');
    }

}
