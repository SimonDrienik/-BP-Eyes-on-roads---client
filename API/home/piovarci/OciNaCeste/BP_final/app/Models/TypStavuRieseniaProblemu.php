<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class TypStavuRieseniaProblemu extends Model
{
    protected $fillable = ['typ_stavu_riesenia_problemu_id', 'nazov'];

    protected $table='typ_stavu_riesenia_problemu';
    protected $primaryKey ='typ_stavu_riesenia_problemu_id';
}
