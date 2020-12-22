<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class KatastralneUzemie extends Model
{
    protected $fillable = ['katastralne_uzemie_id', 'nazov'];

    protected $table='katastralne_uzemie';
    protected $primaryKey ='katastralne_uzemie_id';
}
