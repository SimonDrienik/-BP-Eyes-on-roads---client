<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateFotkaStavuRieseniaProblemuTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('fotka_stavu_riesenia_problemu', function (Blueprint $table) {
            $table->bigIncrements('fotka_stavu_riesenia_problemu_id');
            $table->text('cesta_k_suboru');
            $table->collation = 'utf8mb4_slovak_ci';

        });

        Schema::table('fotka_stavu_riesenia_problemu', function (Blueprint $table) {
            $table->bigInteger('popis_stavu_riesenia_id')->unsigned();


            $table->foreign('popis_stavu_riesenia_id')
                ->references('popis_stavu_riesenia_problemu_id')->on('popis_stavu_riesenia_problemu')
                ->onDelete('cascade');


            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('fotka_stavu_riesenia_problemu');
    }
}
