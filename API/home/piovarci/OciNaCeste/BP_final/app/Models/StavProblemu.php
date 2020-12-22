<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class StavProblemu extends Model
{
    protected $fillable = ['stav_problemu_id', 'nazov'];

    protected $table='stav_problemu';
    protected $primaryKey ='stav_problemu_id';
}
