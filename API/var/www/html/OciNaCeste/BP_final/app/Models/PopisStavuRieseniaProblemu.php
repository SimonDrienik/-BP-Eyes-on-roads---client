<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class PopisStavuRieseniaProblemu extends Model
{
    protected $fillable = ['popis_stavu_riesenia_problemu_id', 'popis', 'problem_id'];

    protected $table='popis_stavu_riesenia_problemu';
    protected $primaryKey ='popis_stavu_riesenia_problemu_id';

    public function Problem(){
        return $this->HasMany('App\Models\Problem', 'problem_id');
    }
}
