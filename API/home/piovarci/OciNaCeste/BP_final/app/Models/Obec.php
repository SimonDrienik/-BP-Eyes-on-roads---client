<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Obec extends Model
{
    protected $fillable = ['obec_id', 'nazov'];

    protected $table='obec';
    protected $primaryKey ='obec_id';
}
